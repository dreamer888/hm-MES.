package com.dream.iot.modbus.server.rtu;

import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusCoilStatus;

/**
 * ModbusRtu协议的Server端报文构建
 */
public class ModbusRtuMessageBuilder {

    /**
     * 构建Modbus读线圈报文
     * @param message
     * @param device 访问的设备
     * @param start 从哪个寄存器开始读
     * @param bitNum 读多少位(一个字节8位)
     * @param <T>
     * @return
     */
    public static <T extends ModbusRtuServerMessage> T buildRead01Message(T message, int device, int start, int bitNum) {
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
    public static <T extends ModbusRtuServerMessage> T buildRead02Message(T message, int device, int start, int bitNum) {
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
    public static <T extends ModbusRtuServerMessage> T buildRead03Message(T message, int device, int start, int num) {
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
    public static <T extends ModbusRtuServerMessage> T buildRead04Message(T message, int device, int start, int num) {
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
    public static <T extends ModbusRtuServerMessage> T buildWrite05Message(T message, int device, int start, ModbusCoilStatus status) {
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
    public static <T extends ModbusRtuServerMessage> T buildWrite06Message(T message, int device, int start, byte[] write) {
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
    public static <T extends ModbusRtuServerMessage> T buildWrite0FMessage(T message, int device, int start, byte[] write) {
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
    public static <T extends ModbusRtuServerMessage> T buildWrite10Message(T message, int device, int start, int num, byte[] write) {
        return doBuildWriteMessage(message, ModbusCode.Write10, device, start, num, write);
    }

    protected static <T extends ModbusRtuServerMessage> T doBuildReadMessage(T message, ModbusCode code, int device, int start, int num) {
        ModbusRtuBody body = ModbusRtuBody.read(code, (short) start, (short)num);
        ModbusRtuHeader header = ModbusRtuHeader.buildRequestHeader((byte) device);

        message.setBody(body);
        message.setHead(header);
        message.setMessage(null);
        header.setEquipCode(message.getEquipCode());
        return message;
    }

    protected static <T extends ModbusRtuServerMessage> T doBuildWriteMessage(T message, ModbusCode code, int device, int start, int num, byte[] write) {
        message.setMessage(null);

        ModbusRtuBody body;
        switch (code) {
            case Write05:
            case Write06:
                body = ModbusRtuBody.writeSingle(code, (short) start, write); break;
            case Write0F:
                body = ModbusRtuBody.write0F((short) start, (short)num, write); break;
            case Write10:
                body = ModbusRtuBody.write10((short) start, (short)num, write); break;

            default: throw new IllegalStateException("不支持写功能码["+code+"]");
        }

        ModbusRtuHeader header = ModbusRtuHeader.buildRequestHeader((byte) device);
        message.setHead(header);
        message.setBody(body);
        header.setEquipCode(message.getEquipCode());

        return message;
    }

}
