package com.dream.iot.server.protocol;

import com.dream.iot.IotServeBootstrap;
import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolType;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.SocketServerComponent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;

/**
 * 服务端同步协议
 * 用于同一连接只能采用同步请求的方式的协议
 * @param <M>
 */
public abstract class ServerInitiativeSyncProtocol<M extends ServerMessage> extends ServerInitiativeProtocol<M>{

    public ServerInitiativeSyncProtocol() {
        this.sync(3000); // 默认同步时间
    }

    @Override
    public void request() throws ProtocolException {
        try {
            this.buildRequestMessage();

            //构建完请求协议必须验证请求数据是否存在
            if(null == requestMessage()) {
                throw new IllegalArgumentException("不存在请求报文");
            }

            //获取客户端管理器
            SocketServerComponent component = IotServeBootstrap.getServerComponent(requestMessage().getClass());
            if(component == null) {
                throw new IllegalArgumentException("获取不到["+responseMessage().getClass().getSimpleName()+"]对应的组件");
            }

            Channel channel = component.getDeviceManager().find(getEquipCode());
            if(channel == null) {
                throw new IllegalStateException("无此设备或设备断线");
            }

            // 死锁校验
            syncDeadValidate(channel);

            // 同步请求处理
            syncRequestHandle(component, channel);

            /**
             * 同一连接使用同步的方式调用
             */
            synchronized (channel) {
                //向客户端写出协议
                ChannelFuture channelFuture = this.writeAndFlush(component);
                if(channelFuture == null) {
                    // 执行业务
                    exec(getProtocolHandle());
                    return;
                }

                /**
                 * 同步请求阻塞线程等待
                 * @see #getTimeout() 等待超时
                 */
                if(isSyncRequest()) {
                    boolean await = getDownLatch().await(getTimeout(), TimeUnit.MILLISECONDS);
                    if(!await) {
                        this.execTimeoutHandle(component);
                    }

                    // 执行业务
                    exec(getProtocolHandle());
                }
            }

        } catch (InterruptedException e) {
            throw new ProtocolException(e, this);
        } catch (Exception e) {
            if(e instanceof ProtocolException) {
                throw e;
            } else {
                throw new ProtocolException("请求失败：" + e.getMessage(), e);
            }
        }
    }

    protected void syncRequestHandle(SocketServerComponent component, Channel channel) { }

    @Override
    public abstract ProtocolType protocolType();
}
