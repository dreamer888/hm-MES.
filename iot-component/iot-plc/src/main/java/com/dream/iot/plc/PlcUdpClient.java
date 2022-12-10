package com.dream.iot.plc;

import com.dream.iot.client.*;
import com.dream.iot.client.component.UdpClientComponent;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.client.udp.UdpClientConnectProperties;
import com.dream.iot.message.DefaultMessageHead;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;

import java.util.function.Consumer;

public abstract class PlcUdpClient extends UdpSocketClient /*implements MultiStageConnect*/ {

    private ChannelPromise connectFinishedPromise;

   /* public PlcUdpClient(UdpClientComponent clientComponent, ClientConnectProperties config) {
        super(clientComponent, config);
    }
*/
    public PlcUdpClient(UdpClientComponent clientComponent, UdpClientConnectProperties config) {
        super(clientComponent, config);
    }





   /* @Override
    public ChannelPromise getConnectFinishedFlag() {
        return this.connectFinishedPromise;
    }

    @Override
    public MultiStageConnect setConnectFinishedFlag(ChannelPromise promise) {
        this.connectFinishedPromise = promise;
        return this;
    }
*/
    @Override
    public ChannelFuture connect(Consumer<?> consumer, long timeout) {
       // return this.stageConnect(consumer == null ? a -> {} : consumer, timeout);
        return this.doConnect(consumer == null ? a -> {} : (Consumer<ChannelFuture>) consumer, timeout);
    }

    @Override
    public ChannelFuture doConnect(Consumer<ChannelFuture> consumer, long timeout) {
        return PlcUdpClient.super.doConnect(consumer, timeout);
    }
}
