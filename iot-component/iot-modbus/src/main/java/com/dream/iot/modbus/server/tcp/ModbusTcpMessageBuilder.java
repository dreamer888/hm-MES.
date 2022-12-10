package com.dream.iot.modbus.server.tcp;

import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusCoilStatus;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ModbusTcp协议的Server端报文构建
 */
public class ModbusTcpMessageBuilder {

    /**
     * 构建Modbus读线圈报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始读
     * @param bitNum 读多少位(一个字节8位)
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildRead01Message(T message, int device, int start, int bitNum) {
        return doBuildReadMessage(message, ModbusCode.Read01, device, start, bitNum);
    }

    /**
     * 构建Modbus读线圈报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始读
     * @param bitNum 读多少位(一个字节8位)
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildRead02Message(T message, int device, int start, int bitNum) {
        return doBuildReadMessage(message, ModbusCode.Read02, device, start, bitNum);
    }

    /**
     * 构建Modbus读保持寄存器报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始读
     * @param num 读几个寄存器
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildRead03Message(T message, int device, int start, int num) {
        return doBuildReadMessage(message, ModbusCode.Read03, device, start, num);
    }

    /**
     * 构建Modbus读输入寄存器报文
     * @param message
     * @param device 访问的设备 (1-255)
     * @param start 从哪个寄存器开始读 (1-65535)
     * @param num 读几个寄存器(1-2000)
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildRead04Message(T message, int device, int start, int num) {
        return doBuildReadMessage(message, ModbusCode.Read04, device, start, num);
    }

    /**
     * 构建Modbus写单个线圈报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始写
     * @param status 写内容
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildWrite05Message(T message, int device, int start, ModbusCoilStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("[status]必填");
        }

        return doBuildWriteMessage(message, ModbusCode.Write05, device, start, 0, status.getCode());
    }

    /**
     * 构建Modbus写单个寄存器报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始写
     * @param write 写内容
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildWrite06Message(T message, int device, int start, byte[] write) {
        return doBuildWriteMessage(message, ModbusCode.Write06, device, start, 0, write);
    }

    /**
     * 构建Modbus写多个线圈报文(按位计算)
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始写
     * @param write 写内容 0xFF = 11111111
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildWrite0FMessage(T message, int device, int start, byte[] write) {
        if(write == null) {
            throw new IllegalArgumentException("[status]必填");
        }

        return doBuildWriteMessage(message, ModbusCode.Write0F, device, start, write.length * 8, write);
    }

    /**
     * 构建Modbus写多个寄存器报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始写
     * @param num 写几个寄存器
     * @param write 写到设备的内容
     * @param <T>
     * @return
     */
    public static <T extends ModbusTcpMessage> T buildWrite10Message(T message, int device, int start, int num, byte[] write) {
        return doBuildWriteMessage(message, ModbusCode.Write10, device, start, num, write);
    }

    protected static <T extends ModbusTcpMessage> T doBuildReadMessage(T message, ModbusCode code, int device, int start, int num) {
        ModbusTcpBody body = ModbusTcpBody.read(code, (short) start, (short)num);
        ModbusTcpHeader header = ModbusTcpHeader.buildRequestHeader((byte) device, (short) body.getLength());

        message.setBody(body);
        message.setHead(header);
        message.setMessage(null);
        header.setEquipCode(message.getEquipCode());
        return message;
    }

    protected static <T extends ModbusTcpMessage> T doBuildWriteMessage(T message, ModbusCode code, int device, int start, int num, byte[] write) {
        message.setMessage(null);

        ModbusTcpBody body;
        switch (code) {
            case Write05:
            case Write06:
                body = ModbusTcpBody.writeSingle(code, (short) start, write); break;
            case Write0F:
                body = ModbusTcpBody.write0F((short) start, (short)num, write); break;
            case Write10:
                body = ModbusTcpBody.write10((short) start, (short)num, write); break;

            default: throw new IllegalStateException("不支持写功能码["+code+"]");
        }

        ModbusTcpHeader header = ModbusTcpHeader.buildRequestHeader((byte) device, (short) body.getLength());
        message.setHead(header);
        message.setBody(body);
        header.setEquipCode(message.getEquipCode());

        return message;
    }

    public static short getNextId(Channel channel) {
        synchronized (channel) {
            Attribute<Object> attribute = channel.attr(ModbusTcpMessage.NEXT_KEY);
            AtomicInteger nextId = (AtomicInteger) attribute.get();
            if(nextId == null || nextId.get() >= Short.MAX_VALUE) {
                attribute.set(nextId = new AtomicInteger(0));
            }

            return (short) nextId.incrementAndGet();
        }
    }

    public static void buildMessageHeadByNextId(short nextId, ModbusTcpHeader header) {
        // 设置Modbus的递增值和messageId
        header.buildNextId(nextId).buildMessageId();
    }
}
