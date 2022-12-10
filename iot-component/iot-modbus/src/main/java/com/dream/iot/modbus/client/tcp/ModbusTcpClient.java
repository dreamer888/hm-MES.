package com.dream.iot.modbus.client.tcp;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpMessageBuilder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;

/**
 *
 * Modbus Tcp协议实现的客户端
 * @see ModbusTcpClientMessage
 */
public class ModbusTcpClient extends TcpSocketClient {

    private int maxFrameLength;

    /**
     * 默认报文最大长度 1024字节
     * @param clientComponent
     * @param config
     */
    public ModbusTcpClient(TcpClientComponent clientComponent, ClientConnectProperties config) {
        this(clientComponent, config, 512);
    }

    public ModbusTcpClient(TcpClientComponent clientComponent, ClientConnectProperties config, int maxFrameLength) {
        super(clientComponent, config);
        this.maxFrameLength = maxFrameLength;
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new LengthFieldBasedFrameMessageDecoderAdapter(this.maxFrameLength, 4, 2, 0, 0, true);
    }

    @Override
    public ChannelFuture writeAndFlush(ClientSocketProtocol clientProtocol) {
        ClientMessage clientMessage = clientProtocol.requestMessage();
        if(clientMessage instanceof ModbusTcpClientMessage && getChannel() != null) {
            short nextId = ModbusTcpMessageBuilder.getNextId(getChannel());
            clientMessage.setChannelId(getChannel().id().asShortText());
            // 使用Channel id作为设备编号
            String equipCode = clientMessage.getChannelId();
            ((ModbusTcpClientMessage) clientMessage).setEquipCode(equipCode);
            ModbusTcpHeader head = (ModbusTcpHeader) clientMessage.getHead();
            head.setEquipCode(equipCode);
            ModbusTcpMessageBuilder.buildMessageHeadByNextId(nextId, head);
        }

        return super.writeAndFlush(clientProtocol);
    }
}
