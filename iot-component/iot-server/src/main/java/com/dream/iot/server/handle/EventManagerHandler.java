package com.dream.iot.server.handle;

import com.dream.iot.CoreConst;
import com.dream.iot.DeviceManager;
import com.dream.iot.IotServeBootstrap;
import com.dream.iot.Message;
import com.dream.iot.codec.filter.CombinedFilter;
import com.dream.iot.codec.filter.RegisterParams;
import com.dream.iot.event.ClientStatus;
import com.dream.iot.event.ClientStatusEvent;
import com.dream.iot.message.UnParseBodyMessage;
import com.dream.iot.server.ServerComponentFactory;
import com.dream.iot.server.SocketServerComponent;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * <p>事件管理处理器</p>
 * 用来管理平台上的各类事件
 * @author dream
 * @since 1.8
 */
@ChannelHandler.Sharable
public class EventManagerHandler extends SimpleChannelInboundHandler<UnParseBodyMessage> {

    private static EventManagerHandler managerHandler;
    private ServerComponentFactory componentFactory;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static EventManagerHandler getInstance(ServerComponentFactory componentFactory) {
        if(managerHandler == null) {
            managerHandler = new EventManagerHandler(componentFactory);
        }
        return managerHandler;
    }

    protected EventManagerHandler(ServerComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    /**
     * 处理心跳事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){ //心跳事件
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state){
                case READER_IDLE: timeoutHandle(ctx, "读超时");
                    break;
                case WRITER_IDLE: timeoutHandle(ctx, "写超时");
                    break;
                case ALL_IDLE: timeoutHandle(ctx, "读写超时");
                    break;
            }

        }
    }

    /**
     * 读写超时处理
     * @param ctx
     */
    protected void timeoutHandle(final ChannelHandlerContext ctx, String desc){
        if(ctx.channel().isActive()) {
            ctx.channel().close().addListener((ChannelFutureListener) future -> {
                String equipCode = (String) ctx.channel().attr(CoreConst.EQUIP_CODE).get();
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                final String target = address.getHostString()+":"+address.getPort();

                InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
                SocketServerComponent component = componentFactory.getByPort(localAddress.getPort());
                if(future.isSuccess()){
                    if(logger.isDebugEnabled()) {
                        logger.debug("客户端超时下线({}) {} - 客户端编号: {} - 客户端地址: {} - 状态: 下线成功"
                                , component.getName(), desc, equipCode, target);
                    }
                } else {
                    logger.warn("客户端超时下线({}) {} - 客户端编号: {} - 客户端地址: {} - 状态: 下线失败", component.getName(), desc, equipCode, target, future.cause());
                }
            });
        }

        return;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnParseBodyMessage msg) throws Exception {
        if(msg.getHead() == null) {
            logger.error("未构建出报文头[{}#doBuild(byte[])]", msg.getClass().getSimpleName());
            return;
        }

        //获取设备编号
        String equipCode = (String) ctx.channel().attr(CoreConst.EQUIP_CODE).get();
        if(null == equipCode) { // 设备编号还没有注册则注册
            SocketServerComponent component = componentFactory.getByClass(msg.getClass());
            DeviceManager deviceManager = component.getDeviceManager();

            CombinedFilter filter = (CombinedFilter) component.getFilter().orElse(CombinedFilter.DEFAULT);
            Message.MessageHead messageHead = filter.register(msg.getHead()
                    , new RegisterParams(msg, ctx.channel(), component));

            equipCode = messageHead.getEquipCode();
            if(equipCode != null) {
                // 注册设备编号到对应的Channel
                Object ifAbsent = ctx.channel().attr(CoreConst.EQUIP_CODE).setIfAbsent(equipCode);
                if(ifAbsent == null) { // 注册成功
                    Channel channel = deviceManager.find(equipCode);
                    // 出现同一个编号两个连接再用的情况
                    if(channel != null && channel != ctx.channel()) {
                        if(channel.isActive()) { // 上一个还未关闭连接
                            if(component.isOverride()) { // 组件已设置了可以覆写上一个连接
                                deviceManager.remove(channel); // 不管关闭成功与否都移除前一个连接
                                channel.attr(CoreConst.CLIENT_OVERRIDE_CLOSED).set(Boolean.valueOf(true)); // 标记此连接因重复而已经被关闭
                                channel.close().syncUninterruptibly(); // 关闭前一个连接
                            }
                        } else {
                            boolean remove = deviceManager.remove(equipCode);// 移除设备连接
                            if(remove && logger.isDebugEnabled()) {
                                logger.warn("客户端冲突({}) - 客户端编号: {} - 处理: 移除早期的一台", component.getName(), messageHead.getEquipCode());
                            }
                        }
                    }

                    deviceManager.add(equipCode, ctx.channel());

                    //触发设备上线事件
                    IotServeBootstrap.publishApplicationEvent(new ClientStatusEvent(equipCode, ClientStatus.online, component));

                    if(logger.isDebugEnabled()) {
                        logger.debug("客户端上线({}) - 客户端编号: {} - 客户端地址: {}", component.getName(), equipCode, ctx.channel().remoteAddress());
                    }
                }
            } else {
                logger.error("注册设备编号({}) 报文头未设置设备编号导致设备注册失败 - 客户端地址: {}", component.getName(), ctx.channel().remoteAddress()); return;
            }
        } else {
            msg.getHead().setEquipCode(equipCode); // 设置设备编号
            if(msg.getHead().getEquipCode() == null) {
                logger.error("注册设备编号 报文头未设置设备编号导致设备注册失败 - 客户端地址: {}", ctx.channel().remoteAddress()); return;
            }
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        InetSocketAddress localAddress = (InetSocketAddress) channel.localAddress();
        SocketServerComponent component = componentFactory.getByPort(localAddress.getPort());
        Optional<CombinedFilter> componentFilter = (Optional<CombinedFilter>) component.getFilter();
        if(!componentFilter.orElse(CombinedFilter.DEFAULT).isActivation(channel, component)) {
            channel.close();
        } else {
            DeviceManager deviceManager = component.getDeviceManager();
            deviceManager.add(channel);

            // 设置客户端上线时间
            channel.attr(CoreConst.CLIENT_ONLINE_TIME).set(System.currentTimeMillis());

            if(logger.isTraceEnabled()) {
                logger.trace("客户端激活 客户端地址：{}", channel.remoteAddress());
            }

            super.channelActive(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {

            Attribute attribute = ctx.channel().attr(CoreConst.EQUIP_CODE);
            InetSocketAddress address = (InetSocketAddress)ctx.channel().localAddress();
            SocketServerComponent serverComponent = componentFactory.getByPort(address.getPort());

            Object equipCode = attribute.get();
            if(equipCode != null && logger.isWarnEnabled()) {
                //触发设备下线事件
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                // 设备不是因为连接重复从而导致的关闭
                Boolean aBoolean = ctx.channel().attr(CoreConst.CLIENT_OVERRIDE_CLOSED).get();
                if(aBoolean == null) {
                    IotServeBootstrap.publishApplicationEvent(new ClientStatusEvent(equipCode, ClientStatus.offline, serverComponent));
                    if(logger.isWarnEnabled()) {
                        logger.warn("server: 客户端断线({}) 连接关闭 - 客户端编号: {} - 客户端地址: {}", serverComponent.getName(), equipCode, remoteAddress);
                    }
                } else {
                    if(logger.isWarnEnabled()) {
                        logger.warn("server:客户端断线({}) 客户端重复导致被剔除下线 - 客户端编号: {} - 客户端地址: {}", serverComponent.getName(), equipCode, remoteAddress);
                    }
                }

            } else if(logger.isWarnEnabled()) {
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                if(remoteAddress != null && serverComponent != null) {
                    // 一般没有设备编号也不会保存到设备管理器
                    logger.warn("server:客户端断线({}) 客户端异常 - 客户端编号: 未注册 - 客户端地址: {}", serverComponent.getName(), remoteAddress);
                }
            }

        } finally {
            super.channelInactive(ctx);
        }

    }
}
