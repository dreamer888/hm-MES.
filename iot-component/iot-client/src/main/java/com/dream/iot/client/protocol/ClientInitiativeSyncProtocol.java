package com.dream.iot.client.protocol;

import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolHandle;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.ClientProtocolException;
import com.dream.iot.client.SocketClient;
import io.netty.channel.ChannelFuture;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 同步的客户端主动协议
 * @param <C>
 */
public abstract class ClientInitiativeSyncProtocol<C extends ClientMessage> extends ClientInitiativeProtocol<C>{

    /**
     * 平台主动向外发起请求
     */
    protected void sendRequest() throws ProtocolException {
        try {
            // 构建请求报文
            buildRequestMessage();

            /**
             * 获取对应的客户端 {@link #getClientKey()}
             */
            SocketClient client = getIotClient();
            synchronized (client) {
                // 检查是否会出现死锁
                syncDeadValidate(client);

                // 发起请求, 写出请求报文
                ChannelFuture request = client.writeAndFlush(this);
                if(!request.isDone()) { // 还没有发送完成
                    if(getTimeout() > 0) {
                        request.get(getTimeout(), TimeUnit.MILLISECONDS); // 此处用来等待报文发送成功
                    }
                } else if(request.cause() != null) { // 发送完成但是失败了
                    throw new ClientProtocolException("写出报文失败：" + request.cause().getMessage(), request.cause());
                }

                /**
                 * 是同步请求
                 */
                if(isSyncRequest()) {
                    // 如果发送成功等待报文响应
                    boolean await = getDownLatch().await(getTimeout(), TimeUnit.MILLISECONDS);
                    if(!await) { // 响应超时
                        this.execTimeoutHandle();
                    }

                    // 同步执行业务
                    ProtocolHandle protocolHandle = getProtocolHandle();
                    if(protocolHandle != null) {
                        protocolHandle.handle(this);
                    }
                } else if(!isRelation()) { // 既不是同步也不是异步, 直接执行业务
                    ProtocolHandle protocolHandle = getProtocolHandle();
                    if(protocolHandle != null) {
                        protocolHandle.handle(this);
                    }
                }
            }

        } catch (InterruptedException e) {
            throw new ClientProtocolException(e);
        } catch (ExecutionException e) {
            if(e.getCause() instanceof ClosedChannelException) {
                throw new ClientProtocolException("请求失败 - 连接关闭", e.getCause());
            } else {
                throw new ClientProtocolException("请求失败 - 知错误", e);
            }
        } catch (TimeoutException e) {
            throw new ClientProtocolException("请求失败 - 超时", e);
        }
    }
}
