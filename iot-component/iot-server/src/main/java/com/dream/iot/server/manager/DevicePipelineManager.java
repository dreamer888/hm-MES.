package com.dream.iot.server.manager;

import com.dream.iot.*;
import com.dream.iot.server.ServerMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.PlatformDependent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * <p>通过设备编号获取以设备关联的链接信息{@link ChannelPipeline}</p>
 *  映射关系: key：为设备编号   value：{@link ChannelPipeline}
 * Create Date By 2017-09-12
 * @author dream
 * @since 1.7
 */
public class DevicePipelineManager extends DefaultChannelGroup implements DeviceManager {

    /**
     * 存储客户端编号和对应的连接
     */
    private final EventExecutor executor;
    private final ConcurrentMap<String, Channel> clientChannels = PlatformDependent.newConcurrentHashMap();
    private static Logger logger = LoggerFactory.getLogger(DevicePipelineManager.class);

    public DevicePipelineManager(String name, EventExecutor executor) {
        super(name, executor);
        this.executor = executor;
    }

    public DevicePipelineManager(String name, EventExecutor executor, boolean stayClosed) {
        super(name, executor, stayClosed);
        this.executor = executor;
    }

    /**
     * 获取一个设备管理实例
     * @return
     */
    public static DeviceManager getInstance(Class<? extends ServerMessage> clazz){
        return IotServeBootstrap.getServerComponent(clazz).getDeviceManager();
    }

    /**
     * 返回所有組件的设备管理器
     * @return
     */
    public static List<DeviceManager> getDeviceManagers() {
        return IotServeBootstrap.COMPONENT_FACTORY.getTcpserverComponents()
                .stream().map(item -> item.getDeviceManager()).collect(Collectors.toList());
    }

    /**
     * 返回所有Tcp连接数
     * @return
     */
    public static int getTcpLink() {
        return IotServeBootstrap.COMPONENT_FACTORY.getTcpserverComponents()
                .stream().map(item -> item.getDeviceManager().size()).reduce((a, b) -> a+b).get();
    }

    /**
     * 返回已在平台注册过设备编号的tcp连接数
     * @return
     */
    public static int getUseTcpLink() {
        return IotServeBootstrap.COMPONENT_FACTORY.getTcpserverComponents()
                .stream().map(item -> item.getDeviceManager().useSize()).reduce((a, b) -> a+b).get();
    }

    /**
     * 获取指定客户端的Tcp连接
     * @param equipCode
     * @return 符合条件的通道列表
     */
    public static List<Channel> findChannel(String equipCode) {
        return IotServeBootstrap.COMPONENT_FACTORY.getTcpserverComponents()
                .stream().map(item -> item.getDeviceManager().find(equipCode)).collect(Collectors.toList());
    }

    /**
     * 获取指定客户端的Tcp连接
     * @param equipCode 客户端编号
     * @param clazz 在哪个服务端组件下查找
     * @return Tcp连接
     */
    public static Channel getChannel(String equipCode, Class<? extends ServerMessage> clazz) {
        return getInstance(clazz).find(equipCode);
    }

    /**
     * 关闭客户端的Tcp连接
     * @param equipCode 要关闭的客户端的客户端编号
     * @param clazz 在哪个服务端组件下查找
     * @return {@link org.springframework.lang.Nullable}
     */
    public static ChannelFuture close(String equipCode, Class<? extends ServerMessage> clazz) {
        Channel channel = getChannel(equipCode, clazz);
        if(channel != null) {
            if(channel.isOpen()) {
                return channel.close();
            } else {
                channel.newSucceededFuture();
            }
        }

        return null;
    }

    @Override
    public boolean add(String equipCode, Channel channel) {
        this.add(channel);
        return clientChannels.putIfAbsent(equipCode, channel) == null;
    }

    @Override
    public int useSize() {
        return clientChannels.size();
    }

    @Override
    public Channel find(String equipCode) {
        return clientChannels.get(equipCode);
    }

    @Override
    public boolean isEmpty() {
        return clientChannels.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if(o instanceof String) {
            return clientChannels.containsKey(o);
        }

        return super.contains(o);
    }

    @Override
    public boolean remove(Object o) {
        Object deviceSn;
        if(o instanceof Channel) {
            // 移除对应的客户端
            deviceSn = ((Channel) o).attr(CoreConst.EQUIP_CODE).get();
            if(deviceSn instanceof String) {
                clientChannels.remove(deviceSn);
            }
        } else if(o instanceof String) {
            o = clientChannels.remove(o);
        } else {
            throw new IllegalArgumentException("只支持客户端key类型[String or Channel]");
        }

        if(logger.isDebugEnabled()) {
            logger.debug("客户端管理器({}) 移除客户端 - 客户端编号: 未注册 - 连接数: {} - 注册数: {}"
                    , name(), o != null ? o : "未注册", size(), clientChannels.size());
        }

        return super.remove(o);
    }

    @Override
    public void clear() {
        super.clear();
        clientChannels.clear();
    }

    @Override
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Object msg, Object... args) {
        if(msg instanceof Protocol) {
            return this.writeAndFlush(equipCode, (Protocol) msg);
        } else {
            if(StringUtils.isBlank(equipCode))
                throw new IllegalArgumentException("设备编号不能为空");
            if(null == msg) {
                throw new IllegalArgumentException("请传入要发送的协议报文");
            }

            Channel channel = find(equipCode);
            if(null == channel) {
                logger.warn("设备在线管理({}) 无此设备或设备断线 - 设备编号: {} - 已连接：{} - 已注册：{}"
                        , this.name(), equipCode, size(), useSize());
                return Optional.ofNullable(null);

            } else if(!channel.isActive()) { // 设备已经取消注册, 删除设备
                remove(equipCode);
                logger.warn("设备在线管理({}) 设备断线 - 设备编号: {} - 已连接：{} - 已注册：{}"
                        , this.name(), equipCode, size(), useSize());
                return Optional.ofNullable(null);
            }

            return Optional.of(channel.writeAndFlush(msg));
        }
    }

    @Override
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Protocol protocol) {
        if(StringUtils.isBlank(equipCode))
            throw new IllegalArgumentException("设备编号不能为空");
        if(null == protocol) {
            throw new IllegalArgumentException("请传入要发送的协议报文");
        }

        Channel channel = find(equipCode);
        if(null == channel) {
            logger.warn("设备在线管理({}) 无此设备或设备断线 - 设备编号: {} - 已连接：{} - 已注册：{} - 协议: {}"
                    , this.name(), equipCode, this.size(), this.useSize(), protocol.protocolType());

            return Optional.ofNullable(null);
        } else if(!channel.isActive()) { // 设备已经取消注册, 删除设备
            remove(equipCode);
            logger.warn("设备在线管理({}) 设备断线 - 设备编号: {} - 已连接：{} - 已注册：{} - 协议: {}"
                    , this.name(), equipCode, this.size(), this.useSize(), protocol.protocolType());
            return Optional.ofNullable(null);
        }

        Message message = protocol.requestMessage();
        if(message instanceof ServerMessage) {
            ((ServerMessage) message).setChannelId(channel.id().asShortText());
        }

        return Optional.of(channel.writeAndFlush(protocol));
    }

    protected EventExecutor getExecutor() {
        return executor;
    }

    protected ConcurrentMap<String, Channel> getClientChannels() {
        return clientChannels;
    }
}
