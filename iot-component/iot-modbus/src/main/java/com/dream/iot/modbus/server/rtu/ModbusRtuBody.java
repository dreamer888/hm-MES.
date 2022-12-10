package com.dream.iot.modbus.server.rtu;

import com.dream.iot.Message;
import com.dream.iot.modbus.ModbusProtocolException;
import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusErrCode;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.utils.ByteUtil;
import com.dream.iot.modbus.ModbusUtil;

public class ModbusRtuBody implements Message.MessageBody {

    private short num; // 寄存器数量
    private short start; // 寄存器(开始)地址
    private byte[] content; // 读/写 内容
    private ModbusCode code; // 功能码

    private byte[] message; // 数据

    /**
     * 错误码
     */
    private ModbusErrCode errCode;

    private static final ModbusRtuBody EMPTY = new ModbusRtuBody();

    protected ModbusRtuBody() {
        this.message = Message.EMPTY;
    }

    public ModbusRtuBody(ModbusCode code, short start, short num) {
        this.code = code;
        this.num = num;
        this.start = start;
    }

    public ModbusRtuBody(ModbusCode code, short start, byte[] content) {
        this.code = code;
        this.start = start;
        this.content = content;
    }

    public ModbusRtuBody(ModbusCode code, short start, short num, byte[] content) {
        this.code = code;
        this.num = num;
        this.start = start;
        this.content = content;
    }

    public static ModbusRtuBody empty() {
        return EMPTY;
    }

    /**
     * 构建读请求报文体
     * @param num 读寄存器数量
     * @param start 寄存器地址(从哪儿开始读)
     * @return
     */
    public static ModbusRtuBody read(ModbusCode code, short start, short num) {
        ModbusRtuBody modbusTcpBody = new ModbusRtuBody();
        modbusTcpBody.num = num;
        modbusTcpBody.code = code;
        modbusTcpBody.start = start;

        modbusTcpBody.message = new byte[7]; // code(1) + start(2) + num(2) + CRC(2)
        ByteUtil.addBytes(modbusTcpBody.message, new byte[]{code.getCode()}, 0);
        ByteUtil.addBytes(modbusTcpBody.message, ByteUtil.getBytesOfReverse(start), 1);
        ByteUtil.addBytes(modbusTcpBody.message, ByteUtil.getBytesOfReverse(num), 3);

        // 计算CRC
        return modbusTcpBody;
    }

    /**
     * 写多保存寄存器报文体
     * @param start
     * @param num
     * @param write
     * @return
     */
    public static ModbusRtuBody write0F(short start, short num, byte[] write) {
        ModbusRtuBody body = new ModbusRtuBody(ModbusCode.Write0F, start, num, write);

        body.message = new byte[1 + 2 + 2 + 1 + write.length + 2]; // code(1) + start(2) + num(2) + length(1) + write(write.length) + CRC

        ByteUtil.addBytes(body.message, new byte[]{ModbusCode.Write0F.getCode()}, 0);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(start), 1);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(num), 3);

        // write长度
        ByteUtil.addBytes(body.message, new byte[]{(byte) write.length}, 5);
        // 写入的内容
        ByteUtil.addBytes(body.message, write, 6);

        /**
         *  @see ModbusRtuServerMessage#writeBuild() 计算CRC
          */

        return body;
    }

    /**
     * 写多保存寄存器报文体
     * @param start
     * @param num
     * @param write
     * @return
     */
    public static ModbusRtuBody write10(short start, short num, byte[] write) {
        ModbusRtuBody body = new ModbusRtuBody(ModbusCode.Write10, start, num, write);

        body.message = new byte[1 + 2 + 2 + 1 + write.length + 2]; // code(1) + start(2) + num(2) + length(1) + write(write.length) + CRC

        ByteUtil.addBytes(body.message, new byte[]{ModbusCode.Write10.getCode()}, 0);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(start), 1);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(num), 3);

        // write长度
        ByteUtil.addBytes(body.message, new byte[]{(byte) write.length}, 5);
        // 写入的内容
        ByteUtil.addBytes(body.message, write, 6);

        /**
         *  @see ModbusRtuServerMessage#writeBuild() 计算CRC
         */
        return body;
    }

    /**
     * 写单个 保存寄存器/线圈 报文体
     * @param start
     * @param write
     * @return
     */
    public static ModbusRtuBody writeSingle(ModbusCode singleCode, short start, byte[] write) {
        ModbusRtuBody body = new ModbusRtuBody(singleCode, start, write);
        body.message = new byte[1 + 2 + write.length + 2]; // code(1) + start(2) + write(write.length) + CRC

        ByteUtil.addBytes(body.message, new byte[]{singleCode.getCode()}, 0);
        ByteUtil.addBytes(body.message, ByteUtil.getBytesOfReverse(start), 1);

        // 写入的内容
        ByteUtil.addBytes(body.message, write, 3);

        /**
         *  @see ModbusRtuServerMessage#writeBuild() 计算CRC
         */

        return body;
    }

    /**
     * @param message 完整的Modbus Rtu报文
     * @return
     */
    public static ModbusRtuBody buildRequestBody(byte[] message) {
        ModbusRtuBody body = new ModbusRtuBody();
        body.message = ByteUtil.subBytes(message, 1);
        body.code = ModbusCode.INSTANCE(body.message[0]);
        body.start = ByteUtil.bytesToShortOfReverse(body.message, 1);

        if(body.code.getCode() == 0x05 || body.code.getCode() == 0x06) {
            body.content = ByteUtil.subBytes(body.message, 3, body.message.length - 2);
        } else if(body.code.getCode() == 0x0F || body.code.getCode() == 0x10) {
            body.num = ByteUtil.bytesToShortOfReverse(body.message, 3);
            body.content = ByteUtil.subBytes(body.message, 6, body.message.length - 2);
        } else {
            body.num = ByteUtil.bytesToShortOfReverse(body.message, 3);
        }

        String crcHex = ModbusUtil.getCRC(ByteUtil.subBytes(message
                , 0, message.length - 2), true);

        String oriCrcHex = ByteUtil.bytesToHex(message, message.length - 2, 2);
        if(!Integer.valueOf(crcHex, 16).equals(Integer.valueOf(oriCrcHex, 16))) {
            throw new ModbusProtocolException("CRC校验失败", body.code);
        }

        return body;
    }

    /**
     * @param message 完整的Modbus Rtu报文
     * @return
     */
    public static ModbusRtuBody buildResponseBody(byte[] message) {
        ModbusRtuBody body = new ModbusRtuBody();
        body.message = message;
        body.content = Message.EMPTY;
        body.message = ByteUtil.subBytes(message, 1);

        int code = (ByteUtil.getByte(message, 1) & 0xFF);
        // 说明操作失败
        if(code > 0x80) {
            // 解析具体的错误信息
            code = code - 0x80;
            body.code = ModbusCode.INSTANCE((byte) code);
            body.errCode = ModbusErrCode.valueOf(message[message.length - 3]);
        } else {
            body.code = ModbusCode.INSTANCE((byte) code);
            // 读响应
            if(body.code.getCode() <= 0x04) {
                body.content = ByteUtil.subBytes(message, 3, message.length - 2);
            } else { // 写响应
                body.content = new byte[0];
            }
        }

        String crcHex = ModbusUtil.getCRC(ByteUtil.subBytes(message
                , 0, message.length - 2), true);
        String oriCrcHex = ByteUtil.bytesToHex(message, message.length - 2, 2);

        if(!Integer.valueOf(crcHex, 16).equals(Integer.valueOf(oriCrcHex, 16))) {
            throw new ModbusProtocolException("CRC校验失败", body.code);
        }

        return body;
    }

    /**
     * 通过ModbusTcpBody构建ModbusRtuBody
     * @param body
     * @return
     */
    public static ModbusRtuBody formModbusTcpBody(ModbusTcpBody body) {
        ModbusRtuBody modbusRtuBody = new ModbusRtuBody();
        byte[] message = new byte[body.getLength() + 2]; //TcpBody.length + 2(CRC)
        modbusRtuBody.num = body.getNum();
        modbusRtuBody.code = body.getCode();
        modbusRtuBody.start = body.getStart();
        modbusRtuBody.content = body.getContent();
        modbusRtuBody.message = ByteUtil.addBytes(message, body.getMessage(), 0);
        return modbusRtuBody;
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
        return ByteUtil.bytesToHex(this.message);
    }

    @Override
    public byte[] getMessage() {
        return this.message;
    }

}
