package com.dream.iot.modbus;

import com.dream.iot.modbus.consts.ModbusCode;
import com.dream.iot.modbus.server.rtu.ModbusRtuBody;
import com.dream.iot.modbus.server.rtu.ModbusRtuHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;

/**
 * Modbus Rtu协议和Tcp协议相互转换工具
 */
public class ModbusRtuOverTcpUtils {

    public static ModbusTcpHeader toTcpHeader(ModbusRtuHeader header, short rtuLength) {
        return ModbusTcpHeader.buildRequestHeader(header.getUnitId(), rtuLength);
    }

    public static ModbusTcpBody toTcpBody(ModbusRtuBody body) {
        if(body.getCode().getCode() <= 0x04) {
            return ModbusTcpBody.read(body.getCode(), body.getStart(), body.getNum());
        } else if(body.getCode() == ModbusCode.Write0F) {
            return ModbusTcpBody.write0F(body.getStart(), body.getNum(), body.getContent());
        } else if(body.getCode() == ModbusCode.Write10) {
            return ModbusTcpBody.write10(body.getStart(), body.getNum(), body.getContent());
        } else {
            return ModbusTcpBody.writeSingle(body.getCode(), body.getStart(), body.getContent());
        }
    }

    public static ModbusRtuBody toRtuBody(ModbusTcpBody body) {
        return ModbusRtuBody.formModbusTcpBody(body);
    }

    public static ModbusRtuHeader toRtuHeader(ModbusTcpHeader head) {
        return ModbusRtuHeader.formModbusTcpHeader(head);
    }

}
