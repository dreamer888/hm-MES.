package com.dream.iot.modbus.client.tcp;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

/**
 * modbus tcp 协议的iot客户端报文
 * @see ModbusTcpMessage modbus协议说明
 */
public class ModbusTcpClientMessage extends ClientMessage implements ModbusTcpMessage {

    /**
     * @see Channel#id()
     * @see ChannelId#asShortText()  作为客户端设备编号
     */
    private String equipCode;

    public ModbusTcpClientMessage() {
        this(EMPTY);
    }

    public ModbusTcpClientMessage(byte[] message) {
        super(message);
    }

    public ModbusTcpClientMessage(ModbusTcpHeader head, ModbusTcpBody body) {
        super(head, body);
    }

    /**
     * modbus服务响应的报文的报文头
     * @param message
     * @return
     */
    @Override
    protected ModbusTcpHeader doBuild(byte[] message) {
        this.messageBody = ModbusTcpBody.buildResponseBody(message);
        ModbusTcpHeader header = ModbusTcpHeader.buildResponseHeader(message);
        header.setEquipCode(this.equipCode);
        return header.buildMessageId();
    }

    @Override
    public String getMessageId() {
        return getHead().getMessageId();
    }

    @Override
    public ModbusTcpBody getBody() {
        return (ModbusTcpBody) super.getBody();
    }

    @Override
    public ModbusTcpHeader getHead() {
        return (ModbusTcpHeader) super.getHead();
    }

    @Override
    public String getEquipCode() {
        return equipCode;
    }

    public ModbusTcpClientMessage setEquipCode(String equipCode) {
        this.equipCode = equipCode;
        return this;
    }
}
