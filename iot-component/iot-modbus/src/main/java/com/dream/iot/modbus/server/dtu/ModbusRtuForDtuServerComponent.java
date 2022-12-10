package com.dream.iot.modbus.server.dtu;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.dtu.DtuMessageType;
import com.dream.iot.server.dtu.SimpleChannelForDtuDecoderComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 适用于：对于Dtu连网的且设备使用Modbus Rtu协议进行数据交互的情况
 * 注意：使用此解码组件需要需用使用同步的方式操作设备(由于不处理粘包的情况)
 */
public class ModbusRtuForDtuServerComponent<M extends ModbusRtuForDtuMessage> extends SimpleChannelForDtuDecoderComponent<M> {

    public ModbusRtuForDtuServerComponent(ConnectProperties connectProperties) {
        this(connectProperties, new ModbusRtuMessageAware());
    }

    public ModbusRtuForDtuServerComponent(ConnectProperties connectProperties, DtuMessageType messageType) {
        this(connectProperties, new ModbusRtuMessageAware(messageType));
    }

    public ModbusRtuForDtuServerComponent(ConnectProperties connectProperties, ModbusRtuMessageAware<M> dtuMessageAwareDelegation) {
        super(connectProperties, dtuMessageAwareDelegation);
    }

    @Override
    public String getName() {
        return "ModbusRtuForDtu";
    }

    @Override
    public String getDesc() {
        return "使用Dtu连网且设备基于标准Modbus Rtu协议的iot服务端实现";
    }

    @Override
    public AbstractProtocol doGetProtocol(ModbusRtuForDtuMessage message) {
        return remove(message.getHead().getMessageId());
    }

    @Override
    public M createMessage(byte[] message) {
        return (M) new ModbusRtuForDtuMessage(message);
    }

    /**
     * 此方法需要配合使用 {@link ModbusRtuMessageAware}
     * @param ctx
     * @param in
     * @return
     */
    @Override
    public M doTcpDecode(ChannelHandlerContext ctx, ByteBuf in) {
        try {
            byte[] message;
            int code = in.getByte(1) & 0xFF;
            // modbus的错误报文
            if(code > 0x80) {
                // 错误报文只读5个字节
                message = new byte[5];
            } else if(code <= 0x04) { // 读报文
                byte length = in.getByte(2);
                message = new byte[3 + length + 2];
            } else { // 写报文固定读取8个字节
                message = new byte[8];
            }

            in.readBytes(message);
            return createMessage(message);
        } finally {
            in.release();
        }
    }
}
