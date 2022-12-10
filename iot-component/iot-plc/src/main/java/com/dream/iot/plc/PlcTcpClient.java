package com.dream.iot.plc;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.MultiStageConnect;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.message.DefaultMessageHead;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;

import java.util.function.Consumer;

public abstract class PlcTcpClient extends TcpSocketClient implements MultiStageConnect {

    private ChannelPromise connectFinishedPromise;

    public PlcTcpClient(TcpClientComponent clientComponent, ClientConnectProperties config) {
        super(clientComponent, config);
    }

    @Override
    public ChannelFuture writeAndFlush(ClientSocketProtocol clientProtocol) {
        if(this.getChannel() != null) {
            // 使用通道id作为messageId
            String messageId = this.getChannel().id().asShortText();
            final ClientMessage clientMessage = clientProtocol.requestMessage();
            DefaultMessageHead head = (DefaultMessageHead) clientMessage.getHead();
            head.setMessageId(messageId);
            clientMessage.setChannelId(messageId);
            head.setEquipCode(this.getConfig().connectKey());
        }

        return super.writeAndFlush(clientProtocol);
    }


    @Override
    public ChannelPromise getConnectFinishedFlag() {
        return this.connectFinishedPromise;
    }

    @Override
    public MultiStageConnect setConnectFinishedFlag(ChannelPromise promise) {
        this.connectFinishedPromise = promise;
        return this;
    }

    @Override
    public ChannelFuture connect(Consumer<?> consumer, long timeout) {
        return this.stageConnect(consumer == null ? a -> {} : consumer, timeout);
    }

    @Override
    public ChannelFuture doConnect(Consumer<ChannelFuture> consumer, long timeout) {
        return PlcTcpClient.super.doConnect(consumer, timeout);
    }
}
