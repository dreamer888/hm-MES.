package com.dream.iot.server.handle;

import com.dream.iot.*;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.client.ClientProxyServerProtocol;
import com.dream.iot.event.ExceptionEvent;
import com.dream.iot.event.ExceptionEventListener;
import com.dream.iot.message.UnParseBodyMessage;
import com.dream.iot.server.SocketServerComponent;
import com.dream.iot.server.ServerComponentFactory;
import com.dream.iot.server.ServerSocketProtocol;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.protocol.NoneDealProtocol;
import com.dream.iot.server.protocol.HeartbeatProtocol;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * <p>协议的业务处理 handler</p>
 * 必须放在netty的最后一个Handler
 * @see io.netty.channel.ChannelPipeline#addLast(ChannelHandler...)
 * Create Date By 2020-09-06
 * @author dream
 * @since 1.7
 */
@ChannelHandler.Sharable
public class ProtocolBusinessHandler extends SimpleChannelInboundHandler<UnParseBodyMessage> {

    private BusinessFactory businessFactory;//业务工厂
    private ServerComponentFactory componentFactory; //组件工厂
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ProtocolBusinessHandler(ServerComponentFactory componentFactory, BusinessFactory businessFactory) {
        this.componentFactory = componentFactory;
        this.businessFactory = businessFactory;
    }

    /**
     * 客户端协议的业务处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnParseBodyMessage msg) throws Exception {

        SocketServerComponent serverComponent = componentFactory.getByClass(msg.getClass());
        if(serverComponent == null) {
            logger.error("获取服务组件失败 对应报文类型: {}", msg.getClass());
            return;
        }

        /**
         *  1. 获取此次请求指定的协议, 通过协议工厂
         *  2. 执行协议动作
         */
        Message.MessageHead head = msg.getHead();
        try {
            AbstractProtocol protocol = serverComponent.protocolFactory().getProtocol(msg);
            // 心跳协议不做任何处理, 只做日志记录
            if(protocol instanceof HeartbeatProtocol) {
                ((HeartbeatProtocol) protocol).setServerComponent(serverComponent).buildRequestMessage();

                // 执行心跳业务, 如果有指定
                ((BusinessAction) protocol).exec(businessFactory);
                return;
            }

            if(protocol instanceof ServerSocketProtocol){
                // 服务端主动请求客户端 客户端的响应报文
                if(protocol instanceof ServerInitiativeProtocol) {
                    protocol.setResponseMessage(msg);
                // 客户端主动向服务端发起的请求
                } else if(protocol instanceof ClientInitiativeProtocol) {
                    if(protocol.requestMessage() == null) {
                        protocol.setRequestMessage(msg);
                    }
                } else { // 交给下一个处理器处理
                    ctx.fireChannelRead(msg);
                }

                if(protocol instanceof BusinessAction) {
                    AbstractProtocol exec = ((BusinessAction)protocol).exec(businessFactory);

                    /**
                     * 如果报文是客户端主动发起请求的报文, 如果执行业务之后有返回响应报文, 则需要写出响应报文到客户端
                     */
                    if(exec instanceof ClientInitiativeProtocol) {
                        writeProtocol(ctx, exec);
                        return;
                    }
                }
            } else if(protocol instanceof NoneDealProtocol) {
                return;
            } else {
                if(logger.isWarnEnabled()) {
                    logger.warn("找不到协议({}) - 协议类型：{} - 客户端编号：{} - messageId: {}"
                            , serverComponent.getName(), head.getType(), head.getEquipCode(), head.getMessageId());
                }
            }
        } catch (Exception e) {
            this.exceptionCaught(ctx, e);
        }
    }

    private void writeProtocol(ChannelHandlerContext ctx, AbstractProtocol exec) {
        final Message.MessageHead responseHead = exec.responseMessage().getHead();

        String desc = exec instanceof ClientProxyServerProtocol ? "平台响应代理客户端" : "平台响应客户端";

        ctx.writeAndFlush(exec).addListener((ChannelFutureListener) future -> {
            String msg1 = future.isSuccess() ? "成功" : "失败";
            if (logger.isDebugEnabled()) {
                logger.debug("{}({}) - 客户端编号: {} - messageId: {} - 协议类型: {}", desc, msg1
                        , responseHead.getEquipCode(), responseHead.getMessageId(), responseHead.getType());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Object o = ctx.channel().attr(CoreConst.EQUIP_CODE).get();
        String equipCode = o == null ? null : (String) o;

        InetSocketAddress address = (InetSocketAddress)ctx.channel().localAddress();
        final SocketServerComponent serverComponent = componentFactory.getByPort(address.getPort());

        logger.error("协议异常({}) - 客户端编号: {} - 错误信息: {} - 处理方式：创建监听器[{}]监听异常事件({})"
                , serverComponent.getName(), equipCode, cause.getMessage()
                , ExceptionEventListener.class.getSimpleName(), ExceptionEvent.class.getSimpleName(), cause);

        IotServeBootstrap.publishApplicationEvent(new ExceptionEvent(cause, equipCode).setComponent(serverComponent));
    }
}
