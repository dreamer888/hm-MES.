package com.dream.iot.modbus.server.dtu;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.Protocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpMessageBuilder;
import com.dream.iot.server.dtu.DtuMessageType;
import com.dream.iot.server.dtu.LengthFieldBasedFrameForDtuDecoderServerComponent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Optional;

/**
 * 适用于：<hr>
 *     首先使用Dtu连网, dtu必须第一个报文必须上报设备编号
 *     其次dtu连接的设备必须是使用标准的Modbus Tcp协议
 * @param <M>
 */
public class ModbusTcpForDtuServerComponent<M extends ModbusTcpForDtuMessage> extends LengthFieldBasedFrameForDtuDecoderServerComponent<M> {

    public ModbusTcpForDtuServerComponent(ConnectProperties connectProperties) {
        this(connectProperties, new ModbusTcpMessageAware<>());
    }

    public ModbusTcpForDtuServerComponent(ConnectProperties connectProperties, DtuMessageType messageType) {
        this(connectProperties, new ModbusTcpMessageAware<>(messageType));
    }

    public ModbusTcpForDtuServerComponent(ConnectProperties connectProperties, ModbusTcpMessageAware<M> dtuMessageAware) {
        this(connectProperties, 1024, dtuMessageAware);
    }

    public ModbusTcpForDtuServerComponent(ConnectProperties connectProperties, int maxFrameLength, DtuMessageType messageType) {
        this(connectProperties, maxFrameLength, new ModbusTcpMessageAware<>(messageType));
    }

    public ModbusTcpForDtuServerComponent(ConnectProperties connectProperties, int maxFrameLength, ModbusTcpMessageAware<M> dtuMessageAware) {
        super(connectProperties, maxFrameLength, 4, 2, 0, 0, true);
        this.setDtuMessageAware(dtuMessageAware);
    }

    @Override
    public String getName() {
        return "ModbusTcpForDtu";
    }

    @Override
    public String getDesc() {
        return "使用Dtu连网且设备基于标准Modbus Tcp协议的iot服务端实现";
    }

    @Override
    public AbstractProtocol doGetProtocol(M message) {
        return remove(message.getHead().getMessageId());
    }

    @Override
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Protocol protocol) {
        Channel channel = getDeviceManager().find(equipCode);
        if(channel != null) {
            ModbusTcpHeader head = (ModbusTcpHeader)protocol.requestMessage().getHead();

            // 设置Modbus的递增值和messageId
            short nextId = ModbusTcpMessageBuilder.getNextId(channel);
            ModbusTcpMessageBuilder.buildMessageHeadByNextId(nextId, head);
        }

        return super.writeAndFlush(equipCode, protocol);
    }

    @Override
    public M createMessage(byte[] message) {
        return (M) new ModbusTcpForDtuMessage(message);
    }
}
