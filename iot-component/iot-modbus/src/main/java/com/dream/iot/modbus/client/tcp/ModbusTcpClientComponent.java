package com.dream.iot.modbus.client.tcp;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientProtocolException;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ModbusTcpClientComponent<M extends ModbusTcpClientMessage> extends TcpClientComponent<M> {

    private static final String DESC = "基于Modbus Tcp协议的Iot客户端实现";

    public ModbusTcpClientComponent() { }

    public ModbusTcpClientComponent(ClientConnectProperties config) {
        super(config);
    }

    public ModbusTcpClientComponent(ClientConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new ModbusTcpClient(this, config);
    }

    @Override
    public SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf in) {
        SocketMessage socketMessage = super.doTcpDecode(ctx, in);
        if(socketMessage instanceof ModbusTcpClientMessage) {
            // 使用Channel id作为设备编号
            return ((ModbusTcpClientMessage) socketMessage).setEquipCode(ctx.channel().id().asShortText());
        } else {
            throw new ClientProtocolException("报文类型必须是["+ModbusTcpClientMessage.class.getSimpleName()+"]或其子类");
        }
    }

    @Override
    public ModbusTcpClientMessage createMessage(byte[] message) {
        return new ModbusTcpClientMessage(message);
    }

    @Override
    public String getName() {
        return "ModbusTcpClient";
    }

    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public AbstractProtocol getProtocol(M message) {
        return remove(message.getHead().getMessageId());
    }

}
