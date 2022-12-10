package com.dream.iot.modbus.client.tcp;

import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.modbus.*;
import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusCoilStatus;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpMessageBuilder;
import com.dream.iot.utils.ByteUtil;
import org.springframework.util.ObjectUtils;

/**
 * Modbus tcp 客户端通用协议
 */
public class ModbusTcpClientCommonProtocol extends ClientInitiativeProtocol<ModbusTcpClientMessage> implements ModbusCommonProtocol {

    private int start;
    private ModbusCode code;
    private Payload payload;

    protected ModbusTcpClientCommonProtocol(ModbusTcpClientMessage message) {
        this.requestMessage = message;
        this.code = message.getBody().getCode();
        this.start = message.getBody().getStart();
    }

    protected ModbusTcpClientCommonProtocol(ModbusCode code, ModbusTcpClientMessage message) {
        this.code = code;
        this.requestMessage = message;
    }

    protected ModbusTcpClientCommonProtocol(int start, ModbusCode code, ModbusTcpClientMessage message) {
        this.code = code;
        this.start = start;
        this.requestMessage = message;
    }

    @Override
    protected ModbusTcpClientMessage doBuildRequestMessage() {
        return this.requestMessage;
    }

    @Override
    public void doBuildResponseMessage(ModbusTcpClientMessage message) {
        if(getExecStatus() == ExecStatus.success) {
            ModbusCode code = message.getBody().getCode();
            byte[] content = message.getBody().getContent();
            // 异常交由开发者自己处理 https://gitee.com/iteaj/iot/issues/I517Y0
            if(message.getBody().getErrCode() != null) {
                return;
            }

            switch (code) {
                case Read01:
                case Read02:
                    this.payload = new RealCoilPayload(this.start, content); break;
                case Read03:
                case Read04:
                    this.payload = new ReadPayload(content, this.start); break;
                default:
                    this.payload = WritePayload.getInstance();
            }
        }
    }

    public Payload getPayload() {
        return payload;
    }

    /**
     * 构建Modbus读线圈协议
     * @see ModbusCode#Read01
     * @param device 从机的设备地址
     * @param start 从哪个寄存器开始读
     * @param num 读多少个
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildRead01(int device, int start, int num) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildRead01Message(new ModbusTcpClientMessage(), device, start, num);
        return new ModbusTcpClientCommonProtocol(num, ModbusCode.Read01, message);
    }

    /**
     * 构建Modbus读线圈协议
     * @see ModbusCode#Read02
     * @param device 从机的设备地址
     * @param start 从哪个寄存器开始读
     * @param num 读多少个
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildRead02(int device, int start, int num) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildRead02Message(new ModbusTcpClientMessage(), device, start, num);
        return new ModbusTcpClientCommonProtocol(num, ModbusCode.Read02, message);
    }

    /**
     * 构建Modbus读保持寄存器报文
     * @param device 从机的设备地址
     * @param start 从哪个寄存器开始读
     * @param num 读几个寄存器
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildRead03(int device, int start, int num) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildRead03Message(new ModbusTcpClientMessage(), device, start, num);
        return new ModbusTcpClientCommonProtocol(start, ModbusCode.Read03, message);
    }

    /**
     * 构建Modbus读输入寄存器协议
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始读 (1-65535)
     * @param num 读几个寄存器(1-2000)
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildRead04(int device, int start, int num) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildRead04Message(new ModbusTcpClientMessage(), device, start, num);
        return new ModbusTcpClientCommonProtocol(start, ModbusCode.Read04, message);
    }

    /**
     * 构建Modbus写单个线圈报文
     * @param device 访问的设备
     * @param start 写哪个寄存器
     * @param status 写内容
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildWrite05(int device, int start, ModbusCoilStatus status) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildWrite05Message(new ModbusTcpClientMessage(), device, start, status);
        return new ModbusTcpClientCommonProtocol(ModbusCode.Write05, message);
    }

    /**
     * 构建Modbus写单个寄存器报文
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写 (1-65535)
     * @param write 写内容
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildWrite06(int device, int start, byte[] write) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildWrite06Message(new ModbusTcpClientMessage(), device, start, write);
        return new ModbusTcpClientCommonProtocol(ModbusCode.Write06, message);
    }

    /**
     * 构建Modbus写单个寄存器报文
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写 (1-65535)
     * @param value 写内容
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildWrite06(int device, int start, short value) {
        byte[] write = ByteUtil.getBytesOfReverse(value);
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildWrite06Message(new ModbusTcpClientMessage(), device, start, write);
        return new ModbusTcpClientCommonProtocol(ModbusCode.Write06, message);
    }

    /**
     * 构建Modbus写多个线圈
     *
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写
     * @param write 写到设备的内容
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildWrite0F(int device, int start, byte[] write) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildWrite0FMessage(new ModbusTcpClientMessage(), device, start, write);
        return new ModbusTcpClientCommonProtocol(ModbusCode.Write0F, message);
    }

    /**
     * 构建Modbus写多个寄存器报文
     * @see com.dream.iot.utils.ByteUtil#getBytes(int)
     * @see com.dream.iot.utils.ByteUtil#getBytes(byte)
     * @see com.dream.iot.utils.ByteUtil#getBytes(long)
     * @see com.dream.iot.utils.ByteUtil#getBytes(float)
     * ......
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写
     * @param num 写几个寄存器
     * @param write 写到设备的内容
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildWrite10(int device, int start, int num, byte[] write) {
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildWrite10Message(new ModbusTcpClientMessage(), device, start, num, write);
        return new ModbusTcpClientCommonProtocol(ModbusCode.Write10, message);
    }

    /**
     * 构建Modbus写多个寄存器报文(字符串类型使用UTF-8)
     * @see com.dream.iot.utils.ByteUtil#getBytes(int)
     * @see com.dream.iot.utils.ByteUtil#getBytes(byte)
     * @see com.dream.iot.utils.ByteUtil#getBytes(long)
     * @see com.dream.iot.utils.ByteUtil#getBytes(float)
     * ......
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写
     * @param args 写到设备的内容(可以是Number类型和String类型) 如果是字符串类型使用UTF-8编码
     * @return
     */
    public static ModbusTcpClientCommonProtocol buildWrite10(int device, int start, Object... args) {
        if(ObjectUtils.isEmpty(args)) {
            throw new ModbusProtocolException("未指定要写的内容", ModbusCode.Write10);
        }

        ModbusUtil.Write10Build write10Build = ModbusUtil.write10Build(args);
        ModbusTcpClientMessage message = ModbusTcpMessageBuilder.buildWrite10Message(
                new ModbusTcpClientMessage(), device, start, write10Build.num, write10Build.message);
        return new ModbusTcpClientCommonProtocol(ModbusCode.Write10, message);
    }

    /**
     * 根据使用自定义的message构建报文
     * @return
     */
    public static ModbusTcpClientCommonProtocol build(ModbusTcpHeader header, ModbusTcpBody body) {
        return new ModbusTcpClientCommonProtocol(new ModbusTcpClientMessage(header, body));
    }

    @Override
    public ModbusCode protocolType() {
        return this.code;
    }

}
