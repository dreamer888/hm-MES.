package com.dream.iot.server;

import com.dream.iot.*;
import com.dream.iot.codec.SocketMessageDecoder;
import com.dream.iot.codec.filter.CombinedFilter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.manager.DevicePipelineManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.ReferenceCounted;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * 用来封装需要监听在TCP端口的设备的所需要的服务端组件
 */
public abstract class SocketServerComponent<M extends ServerMessage, R extends ReferenceCounted>
        implements FrameworkComponent, IotSocketServer, InitializingBean, SocketMessageDecoder<R> {

    private long startTime;
    /**
     * 如果设备编号重复 是否覆改掉上一台
     */
    private boolean override;
    private Class<M> messageClass;
    private Constructor<M> constructor;
    private DeviceManager deviceManager;
    private IotThreadManager threadManager;
    private IotSocketServer iotSocketServer;
    private IotProtocolFactory protocolFactory;
    private ConnectProperties connectProperties;
    private ProtocolTimeoutStorage protocolTimeoutManager;

    public SocketServerComponent(ConnectProperties connectProperties) {
        this.override = true;
        this.connectProperties = connectProperties;
    }

    @Override
    public void finished() {
        FrameworkComponent.super.finished();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 创建设备服务端
     * @return
     */
    public final IotSocketServer deviceServer() {
        if(this.iotSocketServer != null)
            return this.iotSocketServer;

        this.iotSocketServer = createDeviceServer();
        if(this.iotSocketServer == null)
            throw new IllegalStateException("未指定设备服务端对象: " + IotSocketServer.class.getName() + "在设备组件对象中: " + getClass().getSimpleName());

        return iotSocketServer;
    }

    @Override
    public int port() {
        return this.connectProperties.getPort();
    }

    @Override
    public ConnectProperties config() {
        return this.connectProperties;
    }

    protected IotSocketServer createDeviceServer() {
        return this;
    }

    /**
     * 写出报文
     * @param equipCode 设备编号
     * @param msg 发送的协议
     * @param args 自定义参数
     * @return
     */
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Object msg, Object... args) {
        return getDeviceManager().writeAndFlush(equipCode, msg, args);
    }

    /**
     * 写出协议
     * @see Protocol#requestMessage() 请求的报文
     * @see Protocol#responseMessage() 响应的报文
     * @param equipCode 设备编号
     * @param protocol 要写出的协议
     * @return
     */
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Protocol protocol) {
        return this.writeAndFlush(equipCode, protocol, null);
    }

    public DeviceManager getDeviceManager() {
        return this.deviceManager;
    }

    /**
     * 连接通道是否已经关闭
     * @param equipCode
     * @return
     */
    public boolean isClose(String equipCode) {
        Channel channel = this.getDeviceManager().find(equipCode);
        if(channel == null) {
            return true;
        } else {
            return channel.isActive();
        }
    }

    /**
     * 关闭指定设备编号的连接
     * @param equipCode
     * @return
     */
    public Optional<ChannelFuture> close(String equipCode) {
        return Optional.ofNullable(DevicePipelineManager.close(equipCode, getMessageClass()));
    }

    /**
     * 创建协议工厂
     * @return
     */
    public final IotProtocolFactory protocolFactory() {
        if(this.protocolFactory != null) {
            return this.protocolFactory;
        }

        this.protocolFactory = createProtocolFactory();
        if(this.protocolFactory == null) {
            throw new BeanInitializationException("组件["+getClass().getSimpleName()+"]未创建协议工厂实例["+ProtocolFactory.class.getSimpleName()+"]");
        }

        if(this.protocolFactory instanceof ProtocolFactory) {
            ProtocolFactory protocolFactory = (ProtocolFactory) this.protocolFactory;
            if (protocolFactory.getDelegation() == null) {
                protocolFactory.setDelegation(protocolTimeoutStorage());
            }
        }

        return this.protocolFactory;
    }

    protected abstract IotProtocolFactory createProtocolFactory();

    /**
     * 创建设备管理器
     * @return
     */
    protected DeviceManager createDeviceManager() {
        return new DevicePipelineManager(getName(), threadManager.getDeviceManageEventExecutor());
    }

    @Override
    public M createMessage(byte[] message) {
        return BeanUtils.instantiateClass(this.resolveConstructor(), message);
    }

    @Override
    public boolean isDecoder(Channel channel, ReferenceCounted msg) {
        return getFilter().orElse(CombinedFilter.DEFAULT).isDecoder(channel, msg);
    }

    @Override
    public Optional<CombinedFilter> getFilter() {
        return FilterManager.getInstance().getFilter(getClass());
    }

    /**
     * 设备服务端使用的报文类
     * 此类将用来标识唯一的设备服务组件
     * @return
     */
    @Override
    public Class<M> getMessageClass() {
        if(this.messageClass == null) {
            this.messageClass = (Class<M>) GenericTypeResolver
                    .resolveTypeArguments(getClass(), SocketServerComponent.class)[0];

            if(this.messageClass == null) {
                throw new IllegalArgumentException("为指定泛型["+getClass().getSimpleName()+"<M>]");
            }

            this.resolveConstructor();
        }

        return this.messageClass;
    }

    private Constructor<M> resolveConstructor() {
        if(this.constructor == null) {
            try {
                this.constructor = this.getMessageClass().getConstructor(byte[].class);
            } catch (NoSuchMethodException e) {
                final String simpleName = this.getMessageClass().getSimpleName();
                throw new ProtocolException("报文类型缺少构造函数["+simpleName+"(byte[])]", e);
            }
        }

        return this.constructor;
    }

    public final ProtocolTimeoutStorage protocolTimeoutStorage() {
        if(protocolTimeoutManager != null) {
            return this.protocolTimeoutManager;
        }

        return this.protocolTimeoutManager = doCreateProtocolTimeoutStorage();
    }

    /**
     * 创建超时管理器
     * @return
     */
    protected ProtocolTimeoutStorage doCreateProtocolTimeoutStorage() {
        return new ProtocolTimeoutStorage(getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        deviceServer();
        protocolFactory();
        this.deviceManager = createDeviceManager();
    }

    @Autowired
    protected void setThreadManager(IotThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    /**
     * 覆盖上一台相同设备编号的设备
     * @return
     */
    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
