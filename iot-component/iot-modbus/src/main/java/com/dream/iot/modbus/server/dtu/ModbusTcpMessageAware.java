package com.dream.iot.modbus.server.dtu;

import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.consts.ModbusErrCode;
import com.dream.iot.server.dtu.DefaultDtuMessageAware;
import com.dream.iot.server.dtu.DtuCommonProtocolType;
import com.dream.iot.server.dtu.DtuMessageDecoder;
import com.dream.iot.server.dtu.DtuMessageType;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.ByteBuf;

public class ModbusTcpMessageAware<M extends ModbusTcpForDtuMessage> extends DefaultDtuMessageAware<M> {

    public ModbusTcpMessageAware() { }

    public ModbusTcpMessageAware(DtuMessageType messageType) {
        super(messageType);
    }

    public ModbusTcpMessageAware(DtuMessageDecoder decoder) {
        super(decoder);
    }

    @Override
    protected M customizeType(String equipCode, byte[] message, ByteBuf msg) {
        // modbus tcp 协议报文长度不能小于8
        // 小于8说明是dtu的报文
        if(message.length < 8) {
            return null; // 不能校验 等待下一个报文
        }

        // 获取modbus tcp的功能码
        int code = message[7] & 0xFF;
        if(code > 0x80) { // 读取到的功能码可能是modbus的异常码, 异常码大于 0x80
            code = code - 0x80;
            try {
                ModbusCode.INSTANCE((byte) code);
                ModbusErrCode.valueOf(message[8]);

                // 交由下个modbus协议处理器处理
                return createMessage(DtuCommonProtocolType.PASSED, msg);
            } catch (IllegalStateException e) {
                return createMessage(DtuCommonProtocolType.DTU, msg);
            }
        }

        // modbus的功能码只能是在 0x01 - 0x10之间
        if(code >= 0x01 && code <= 0x10) {
            try {
                ModbusCode.INSTANCE((byte) code);

                short length = ByteUtil.bytesToShortOfReverse(message, 4);
                if(message.length < length + 6) {
                    if(isDtu(message)) {
                        return createMessage(DtuCommonProtocolType.DTU, msg);
                    } else {
                        return null;
                    }
                }

                // 交由下个modbus协议解码器解析modbus报文
                return createMessage(DtuCommonProtocolType.PASSED, msg);
            } catch (IllegalStateException e) { // 不是modbus协议的功能码
                return createMessage(DtuCommonProtocolType.DTU, msg);
            }
        } else { // 不存在功能码说明不是modbus协议
            return createMessage(DtuCommonProtocolType.DTU, msg);
        }
    }

    /**
     * 如果长度不够校验是否时dtu报文
     * @param message
     * @return 默认 false(说明是modbus的拆包等待下一包的到来)
     */
    protected boolean isDtu(byte[] message) {
        return false;
    }
}
