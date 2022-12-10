package com.dream.iot.modbus.server.tcp;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.Message;
import com.dream.iot.Protocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.LengthFieldBasedFrameDecoderServerComponent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.Optional;

/**
 * 标准的Modbus Tcp服务组件
 * @param <M>
 */
public abstract class ModbusTcpServerComponent<M extends ModbusTcpServerMessage> extends LengthFieldBasedFrameDecoderServerComponent<M> {

    public ModbusTcpServerComponent(ConnectProperties connectProperties) {
        this(connectProperties, 512);
    }

    public ModbusTcpServerComponent(ConnectProperties connectProperties, int maxFrameLength) {
        super(connectProperties, maxFrameLength, 4, 2, 0, 0, true);
    }

    @Override
    public String getName() {
        return "ModbusTcpServer";
    }

    @Override
    public String getDesc() {
        return "标准ModbusTcp协议的iot服务端实现";
    }

    @Override
    public AbstractProtocol getProtocol(ModbusTcpServerMessage message) {
        Message.MessageHead head = message.getHead();
        return remove(head.getMessageId());
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

}
