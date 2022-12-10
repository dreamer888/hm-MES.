package com.dream.iot.modbus.server.dtu;

import com.dream.iot.ProtocolType;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.modbus.ModbusCommonProtocol;
import com.dream.iot.modbus.ModbusProtocolException;
import com.dream.iot.modbus.ModbusUtil;
import com.dream.iot.modbus.Payload;
import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusCoilStatus;
import com.dream.iot.modbus.server.rtu.ModbusRtuBody;
import com.dream.iot.modbus.server.rtu.ModbusRtuHeader;
import com.dream.iot.modbus.server.rtu.ModbusRtuMessageBuilder;
import com.dream.iot.server.SocketServerComponent;
import com.dream.iot.server.dtu.DtuCommonProtocol;
import com.dream.iot.server.protocol.ServerInitiativeSyncProtocol;
import com.dream.iot.utils.ByteUtil;
import io.netty.channel.Channel;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * 用来处理基于Modbus Rtu协议设备的通用读写操作<hr>
 *     1. 也可以自行创建协议进行操作
 *     2. 此协议只能使用同步操作
 * @see #request() 对于同一个客户端都使用同步的方式操作设备
 */
public class ModbusRtuForDtuCommonProtocol extends ServerInitiativeSyncProtocol<ModbusRtuForDtuMessage> implements ModbusCommonProtocol, DtuCommonProtocol {

    /**
     * 寄存器开始地址
     */
    private int start;

    private Payload payload;

    private ProtocolType type;

    protected ModbusRtuForDtuCommonProtocol(ProtocolType type, ModbusRtuForDtuMessage message) {
        this.type = type;
        this.setRequestMessage(message);
    }

    protected ModbusRtuForDtuCommonProtocol(ModbusCode code, ModbusRtuForDtuMessage message) {
        this(0, code, message);
    }

    protected ModbusRtuForDtuCommonProtocol(int start, ModbusCode code, ModbusRtuForDtuMessage message) {
        this.type = code;
        this.start = start;
        this.setRequestMessage(message);
    }

    @Override
    protected void syncRequestHandle(SocketServerComponent component, Channel channel) {
        requestMessage().getHead().setMessageId(channel.id().asShortText());
    }

    @Override
    protected ModbusRtuForDtuMessage doBuildRequestMessage() throws IOException {
        return this.requestMessage;
    }

    @Override
    protected void doBuildResponseMessage(ModbusRtuForDtuMessage message) {
        if(this.getExecStatus() == ExecStatus.success
                && this.protocolType() instanceof ModbusCode) { // 属于Modbus协议
            ModbusRtuBody body = message.getBody();
            if(body.getErrCode() != null) {
                return;
            }

            this.payload = ModbusUtil.resolvePayload(body.getContent(), (short) this.start, body.getCode());
        }
    }

    public Payload getPayload() {
        return payload;
    }

    /**
     * 构建自定义报文
     * @param equipCode 要操作的设备的设备编号
     * @param message
     * @param type
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol build(String equipCode, byte[] message, ProtocolType type) {
        ModbusRtuForDtuMessage forDtuMessage = new ModbusRtuForDtuMessage(equipCode);
        forDtuMessage.setMessage(message);
        forDtuMessage.setBody(ModbusRtuBody.empty());
        forDtuMessage.setHead(ModbusRtuHeader.buildRequestHeader(equipCode, null, type));
        return new ModbusRtuForDtuCommonProtocol(type, forDtuMessage);
    }

    /**
     * 构建Modbus读线圈协议
     * @see ModbusCode#Read01
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址
     * @param start 从哪个寄存器开始读
     * @param num 读多少个
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildRead01(String equipCode, int device, int start, int num) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildRead01Message(new ModbusRtuForDtuMessage(equipCode), device, start, num);
        return new ModbusRtuForDtuCommonProtocol(num, ModbusCode.Read01, message);
    }

    /**
     * 构建Modbus读线圈协议
     * @see ModbusCode#Read02
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址
     * @param start 从哪个寄存器开始读
     * @param num 读多少个
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildRead02(String equipCode, int device, int start, int num) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildRead02Message(new ModbusRtuForDtuMessage(equipCode), device, start, num);
        return new ModbusRtuForDtuCommonProtocol(num, ModbusCode.Read02, message);
    }

    /**
     * 构建Modbus读保持寄存器报文
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址
     * @param start 从哪个寄存器开始读
     * @param num 读几个寄存器
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildRead03(String equipCode, int device, int start, int num) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildRead03Message(new ModbusRtuForDtuMessage(equipCode), device, start, num);
        return new ModbusRtuForDtuCommonProtocol(start, ModbusCode.Read03, message);
    }

    /**
     * 构建Modbus读输入寄存器协议
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始读 (1-65535)
     * @param num 读几个寄存器(1-2000)
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildRead04(String equipCode, int device, int start, int num) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildRead04Message(new ModbusRtuForDtuMessage(equipCode), device, start, num);
        return new ModbusRtuForDtuCommonProtocol(start, ModbusCode.Read04, message);
    }

    /**
     * 构建Modbus写单个线圈报文
     * @param equipCode 要操作的设备的设备编号
     * @param device 访问的设备
     * @param start 从哪个寄存器开始写
     * @param status 写内容
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildWrite05(String equipCode, int device, int start, ModbusCoilStatus status) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildWrite05Message(new ModbusRtuForDtuMessage(equipCode), device, start, status);
        return new ModbusRtuForDtuCommonProtocol(ModbusCode.Write05, message);
    }

    /**
     * 构建Modbus写单个寄存器报文
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写 (1-65535)
     * @param write 写内容
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildWrite06(String equipCode, int device, int start, byte[] write) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildWrite06Message(new ModbusRtuForDtuMessage(equipCode), device, start, write);
        return new ModbusRtuForDtuCommonProtocol(ModbusCode.Write06, message);
    }

    /**
     * 构建Modbus写单个寄存器报文
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写 (1-65535)
     * @param value 写内容
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildWrite06(String equipCode, int device, int start, short value) {
        byte[] write = ByteUtil.getBytesOfReverse(value);
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildWrite06Message(new ModbusRtuForDtuMessage(equipCode), device, start, write);
        return new ModbusRtuForDtuCommonProtocol(ModbusCode.Write06, message);
    }

    /**
     * 构建Modbus写多个寄存器报文
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写
     * @param write 写到设备的内容 1Byte = 8bit = 8个线圈
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildWrite0F(String equipCode, int device, int start, byte[] write) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildWrite0FMessage(new ModbusRtuForDtuMessage(equipCode), device, start, write);
        return new ModbusRtuForDtuCommonProtocol(ModbusCode.Write0F, message);
    }

    /**
     * 构建Modbus写多个寄存器报文
     * @param equipCode 要操作的设备的设备编号
     * @param device 从机的设备地址 (1-255)
     * @param start 从哪个寄存器开始写
     * @param num 写几个寄存器
     * @param write 写到设备的内容
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildWrite10(String equipCode, int device, int start, int num, byte[] write) {
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildWrite10Message(new ModbusRtuForDtuMessage(equipCode), device, start, num, write);
        return new ModbusRtuForDtuCommonProtocol(ModbusCode.Write10, message);
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
     * @param equipCode 要操作的设备的设备编号
     * @param args 写到设备的内容(可以是Number类型和String类型) 如果是字符串类型使用UTF-8编码
     * @return
     */
    public static ModbusRtuForDtuCommonProtocol buildWrite10(String equipCode, int device, int start, Object... args) {
        if(ObjectUtils.isEmpty(args)) {
            throw new ModbusProtocolException("未指定要写的内容", ModbusCode.Write10);
        }

        ModbusUtil.Write10Build write10Build = ModbusUtil.write10Build(args);
        ModbusRtuForDtuMessage message = ModbusRtuMessageBuilder.buildWrite10Message(
                new ModbusRtuForDtuMessage(equipCode), device, start, write10Build.num, write10Build.message);
        return new ModbusRtuForDtuCommonProtocol(ModbusCode.Write10, message);
    }

    public int getStart() {
        return start;
    }

    @Override
    public ProtocolType protocolType() {
        return this.type;
    }
}
