package com.dream.iot.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * 多阶段链接
 */
public interface MultiStageConnect {

    AttributeKey<Boolean> CONN_INIT_FLAG = AttributeKey.valueOf("IOT:MSC:CONN_INIT_FLAG");

    Channel getChannel();

    ChannelPromise getConnectFinishedFlag();

    MultiStageConnect setConnectFinishedFlag(ChannelPromise promise);

    default ChannelPromise setSuccess() {
        this.getChannel().attr(CONN_INIT_FLAG).set(true);
        final ChannelPromise channelPromise = getConnectFinishedFlag();
        if(channelPromise != null) {
            this.setConnectFinishedFlag(null);
            channelPromise.setSuccess();
        }

        return channelPromise;
    }

    default ChannelPromise setFailure(Throwable cause) {
        final ChannelPromise channelPromise = getConnectFinishedFlag();
        if(channelPromise != null) {
            this.setConnectFinishedFlag(null);
            channelPromise.setFailure(cause);
        }

        return channelPromise;
    }

    /**
     * 多阶段连接
     * @param consumer
     * @param timeout
     * @return
     */
    default ChannelFuture stageConnect(Consumer<?> consumer, long timeout) {
        // 说明此客户端已经发起了连接但是还没有完成整个初始化
        if(this.getConnectFinishedFlag() != null) {
            return this.getConnectFinishedFlag();
        }

        // 连接未初始化或者没有激活
        if(this.getChannel() == null || !this.getChannel().isActive()) {
            ChannelFuture connect = this.doConnect((Consumer<ChannelFuture>) consumer, timeout);
            final ChannelPromise returnPromise = connect.channel().newPromise();
            connect.addListener(future -> {
                Attribute<Boolean> attr = connect.channel().attr(CONN_INIT_FLAG);
                if(future.isSuccess()) {
                    if(attr.get() == null) {

                        /**
                         * @see #connAck(MqttConnAckMessage, ChannelHandlerContext) 改变{@code #writeable}状态
                         * mqtt在连接之后还不能直接发布报文(会导致mqtt服务器主动断开连接), 需要等待服务端ConnAck
                         */
                        if(attr.get() == null && this.getConnectFinishedFlag() == null) {
                            long ackTimeout = timeout == 0 ? 3000 : timeout;
                            synchronized (this) {
                                if(attr.get() == null && this.getConnectFinishedFlag() == null) {
                                    this.setConnectFinishedFlag(returnPromise); // 用来标记此连接未初始化
                                    connect.channel().eventLoop().schedule(() -> {
                                        if(attr.get() == null) { // 初始化失败
                                            this.setFailure(new TimeoutException("连接初始化超时("+ackTimeout+"ms)"));
                                        }
                                    }, ackTimeout, TimeUnit.MILLISECONDS);
                                }
                            }
                        }
                    }
                } else {
                    returnPromise.setFailure(future.cause());
                }
            });

            return returnPromise;
        } else {
            Attribute<Boolean> attr = this.getChannel().attr(CONN_INIT_FLAG);
            if(attr.get() != null) { // 说明已经完成初始化
                return this.getChannel().newSucceededFuture();
            } else { // 未初始化完成
                final ChannelPromise promise = getConnectFinishedFlag();
                if(promise != null) {
                    return promise;
                } else if(attr.get() != null) { // 已经完成且成功
                    return this.getChannel().newSucceededFuture();
                } else { // 已经完成但是失败了
                    return this.getChannel().newFailedFuture(new ClientProtocolException("初始化失败"));
                }
            }
        }
    }

    ChannelFuture doConnect(Consumer<ChannelFuture> consumer, long timeout);
}
