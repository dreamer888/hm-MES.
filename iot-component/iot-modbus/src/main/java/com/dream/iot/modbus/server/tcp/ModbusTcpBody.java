package com.dream.iot.modbus.server.tcp;

import com.dream.iot.Message;
import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusErrCode;
import com.dream.iot.utils.ByteUtil;

public class ModbusTcpBody implements Message.MessageBody {

    private short num; // 寄存器数量
    private short start; // 寄存器(开始)地址
    private byte[] content; // 读/写 内容
    private ModbusCode code; // 功能码

    private byte[] message; // 数据

    /**
     * 错误码
     */
    private ModbusErrCode errCode;

    private static ModbusTcpBody EMPTY = new ModbusTcpBody();

    protected ModbusTcpBody() {
        this.message = Message.EMPTY;
    }

    public ModbusTcpBody(ModbusCode code, short start, short num) {
        this.code = code;
        this.num = num;
        this.start = start;
    }

    public ModbusTcpBody(ModbusCode code, short start, byte[] content) {
        this.code = code;
        this.start = start;
        this.content = content;
    }

    public ModbusTcpBody(ModbusCode code, short start, short num, byte[] content) {
        this.code = code;
        this.num = num;
        this.start = start;
        this.content = content;
    }

    public static ModbusTcpBody empty() {
        return ModbusTcpBody.EMPTY;
    }

    /**
     * 构建读请求报文体
     * @param num 读寄存器数量
     * @param start 寄存器地址(从哪儿开始读)
     * @return
     */
    public static ModbusTcpBody read(ModbusCode code, short start, short num) {
        ModbusTcpBody modbusTcpBody = new ModbusTcpBody();
        modbusTcpBody.num = num;
        modbusTcpBody.code = code;
        modbusTcpBody.start = start;

        modbusTcpBody.message = new byte[5]; // code(1) + start(2) + num(2)
        ByteUtil.addBytes(modbusTcpBody.message, new byte[]{code.getCode()}, 0);
        ByteUtil.addBytes(modbusTcpBody.message, ByteUtil.getBytesOfReverse(start), 1);
        ByteUtil.addBytes(modbusTcpBody.message, ByteUtil.getBytesOfReverse(num), 3);
        return modbusTcpBody;
    }

    /**
     * 写多保存寄存器报文体
     * @param start
     * @param num
     * @param write
     * @return
     */
    public static ModbusTcpBody write0F(short start, short num, byte[] write) {
        ModbusTcpBody body = new ModbusTcpBody(ModbusCode.Write0F, start, num, write);

        body.message = new byte[1 + 2 + 2 + 1 + write.length]; // code(1) + start(2) + num(2) + length(1) + write(write.length)

        ByteUtil.addBytes(body.message, new byte[]{ModbusCode.Write0F.getCode()}, 0);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(start), 1);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(num), 3);

        // write长度
        ByteUtil.addBytes(body.message, new byte[]{(byte) write.length}, 5);
        // 写入的内容
        ByteUtil.addBytes(body.message, write, 6);
        return body;
    }

    /**
     * 写多保存寄存器报文体
     * @param start
     * @param num
     * @param write
     * @return
     */
    public static ModbusTcpBody write10(short start, short num, byte[] write) {
        ModbusTcpBody body = new ModbusTcpBody(ModbusCode.Write10, start, num, write);

        body.message = new byte[1 + 2 + 2 + 1 + write.length]; // code(1) + start(2) + num(2) + length(1) + write(write.length)

        ByteUtil.addBytes(body.message, new byte[]{ModbusCode.Write10.getCode()}, 0);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(start), 1);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(num), 3);

        // write长度
        ByteUtil.addBytes(body.message, new byte[]{(byte) write.length}, 5);
        // 写入的内容
        ByteUtil.addBytes(body.message, write, 6);
        return body;
    }

    /**
     * 写单个 保存寄存器/线圈 报文体
     * @param start
     * @param write
     * @return
     */
    public static ModbusTcpBody writeSingle(ModbusCode singleCode, short start, byte[] write) {
        ModbusTcpBody body = new ModbusTcpBody(singleCode, start, write);
        body.message = new byte[1 + 2 + write.length]; // code(1) + start(2) + write(write.length)

        ByteUtil.addBytes(body.message, new byte[]{singleCode.getCode()}, 0);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(start), 1);

        // 写入的内容
        ByteUtil.addBytes(body.message, write, 3);
        return body;
    }

    public static ModbusTcpBody buildResponseBody(byte[] message) {
        ModbusTcpBody body = new ModbusTcpBody();
        body.message = message;
        body.content = Message.EMPTY;
        body.message = ByteUtil.subBytes(message, 7);

        int code = (ByteUtil.getByte(message, 7) & 0xFF);
        // 说明操作失败
        if(code > 0x80) {
            // 解析具体的错误信息
            code = code - 0x80;
            body.code = ModbusCode.INSTANCE((byte) code);
            body.errCode = ModbusErrCode.valueOf(message[message.length - 1]);
        } else {
            body.code = ModbusCode.INSTANCE((byte) code);
            if(body.code.getCode() <= 0x04) {
                body.content = ByteUtil.subBytes(message, 9);
            } else {
                body.content = new byte[0];
            }
        }

        return body;
    }

    public static ModbusTcpBody copy(ModbusTcpBody body) {
        ModbusTcpBody modbusTcpBody = new ModbusTcpBody();
        modbusTcpBody.num = body.getNum();
        modbusTcpBody.code = body.getCode();
        modbusTcpBody.start = body.getStart();
        modbusTcpBody.errCode = body.getErrCode();

        modbusTcpBody.content = body.getContent() != null ? body.getContent().clone() : null;
        modbusTcpBody.message = body.getMessage() != null ? body.getMessage().clone() : null;
        return modbusTcpBody;
    }

    public boolean isSuccess() {
        return this.errCode == null;
    }

    public short getStart() {
        return start;
    }

    public void setStart(short start) {
        this.start = start;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public ModbusCode getCode() {
        return code;
    }

    public void setCode(ModbusCode code) {
        this.code = code;
    }

    public ModbusErrCode getErrCode() {
        return errCode;
    }

    public void setErrCode(ModbusErrCode errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "ModbusTcpBody{" +
                "num=" + num +
                ", start=" + start +
                ", code=" + code +
                '}' +
                " HEX=" + (this.getMessage() != null ? ByteUtil.bytesToHexByFormat(this.getMessage()) : "");
    }

    @Override
    public byte[] getMessage() {
        return this.message;
    }

}
