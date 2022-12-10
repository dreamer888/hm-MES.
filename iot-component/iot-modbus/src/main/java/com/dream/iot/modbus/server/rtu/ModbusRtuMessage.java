package com.dream.iot.modbus.server.rtu;

import com.dream.iot.Message;

/**
 *  标准的modbus Rtu报文协议格式
 *
 *  读请求报文格式：
 *
 *                          01      03    00 01  00 00  |   00 00
 *                      | 从机地址 |功能码|起始地址|寄存器数量|    CRC
 *  |            header           |             body              |
 *
 *  读响应报文格式：
 *                      |     01     03     01     00 00  |  00 00
 *                      |  从机地址 |功能码|字节个数|请求的数据 |   CRC
 *  |            header           |              body            |
 *
 *  写请求报文格式：
 *
 *                          01      06    00 00  00 00      00 00   ... | 00 00
 *                      | 从机地址 |功能码|起始地址|寄存器数量| 正文长度 | 正文 |  CRC
 *  |            header          |                    body                     |
 *
 *  写响应报文格式：
 *
 *                           01      06   00 00  00 00  |  00 00
 *                        从机地址 |功能码|起始地址|寄存器数量|   CRC
 *  |            header          |             body             |
 *
 * 从机地址：	从机设备地址
 *
 * 功能码 含义	   功能码	    含义
 * 0x01	读线圈	    0x04	读输入寄存器
 * 0x05	写单个线圈	0x03	读保持寄存器
 * 0x0F	写多个线圈	0x06	写单个保持寄存器
 * 0x02	读离散量输入	0x10	写多个保持寄存器
 *
 * @see com.dream.iot.modbus.consts.ModbusCode
 */
public interface ModbusRtuMessage extends Message {

    @Override
    ModbusRtuBody getBody();

    @Override
    ModbusRtuHeader getHead();

    String getEquipCode();
}
