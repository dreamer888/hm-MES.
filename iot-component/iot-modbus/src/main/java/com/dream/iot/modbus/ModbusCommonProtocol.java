package com.dream.iot.modbus;

import com.dream.iot.Protocol;
import com.dream.iot.ProtocolType;
import com.dream.iot.modbus.consts.ModbusCode;

/**
 * 居于iot框架实现的通用的Modbus操作协议
 */
public interface ModbusCommonProtocol extends Protocol {

    Payload getPayload();

    /**
     * @see ModbusCode or other
     * @return
     */
    ProtocolType protocolType();
}
