package com.dream.iot.client.proxy;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.proxy.ProxyClientMessage;
import com.dream.iot.utils.UniqueIdGen;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

public class ProxyClient extends TcpSocketClient {

    private String deviceSn;
    private ScheduledFuture<?> scheduledFuture;

    public ProxyClient(TcpClientComponent clientComponent, ClientProperties.ClientProxyConnectProperties config) {
        super(clientComponent, config);
        final String deviceSn = config.getDeviceSn();
        this.deviceSn = deviceSn != null ? deviceSn : UniqueIdGen.deviceSn();
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        ClientProperties.ClientProxyConnectProperties config = (ClientProperties.ClientProxyConnectProperties) getConfig();
        return new LengthFieldBasedFrameMessageDecoderAdapter(ByteOrder.LITTLE_ENDIAN, config.getMaxFrameLength()
                , 0, 4, 0, 4, true) {

            @Override
            public Class<? extends SocketMessage> getMessageClass() {
                return ProxyClientMessage.class;
            }
        };
    }

    /**
     * 连接成功之后发送心跳包
     * @param future
     */
    @Override
    public void successCallback(ChannelFuture future) {
        ClientProperties.ClientProxyConnectProperties config = (ClientProperties.ClientProxyConnectProperties) getConfig();
        // 心跳周期大于0则启用心跳
        if(config.getHeart() > 0 && this.scheduledFuture == null) {
            this.scheduledFuture = future.channel().eventLoop().scheduleAtFixedRate(() -> {
                try {
                    new ProxyClientProtocol(ProxyClientMessage.heart()).timeout(0).request();
                } catch (Exception e) {
                    logger.error("代理客户端心跳请求异常", e);
                }
            }, 10, config.getHeart(), TimeUnit.SECONDS);
        }
    }

    /**
     * 客户端移除的时候移除心跳任务
     * todo 优化
     */
    @Override
    protected void disconnectSuccessCall(boolean remove) {
        if(this.scheduledFuture != null && !this.scheduledFuture.isCancelled()) {
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
        }
    }

    public String getDeviceSn() {
        return deviceSn;
    }
}
