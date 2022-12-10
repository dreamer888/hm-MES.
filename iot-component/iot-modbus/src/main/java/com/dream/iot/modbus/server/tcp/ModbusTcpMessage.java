package com.dream.iot.modbus.server.tcp;

import com.dream.iot.Message;
import io.netty.util.AttributeKey;

/**
 *  标准的modbus tcp报文协议格式
 *
 *  读请求报文格式：
 *  |             MBAP  |           MODBUS RTU
 *   00 00  00 00  00 06    01      03    00 01  00 00
 *  | 事务 |协议标识| 长度 | 从机地址 |功能码|起始地址|寄存器数量|
 *  |               header       |         body         |
 *
 *  读响应报文格式：
 *   00 00  00 00  00 06     01     83      01     00 00
 *  | 事务 |协议标识| 长度 | 从机地址 |功能码|字节个数|请求的数据|
 *  |               header       |         body         |
 *
 *  写请求报文格式：
 *  |             MBAP  |           MODBUS RTU
 *   00 00  00 00  00 06    01      06    00 00  00 00      00 00   ...
 *  | 事务 |协议标识| 长度 | 从机地址 |功能码|起始地址|寄存器数量| 正文长度 | 正文 |
 *  |               header             |              body              |
 *
 *  写响应报文格式：
 *  |             MBAP   |              MODBUS RTU
 *   00 00  00 00  00 06    01      06    00 00  00 00
 *  | 事务 |协议标识| 长度 | 从机地址 |功能码|起始地址|寄存器数量|
 *  |               header             |       body     |
 *
 * 事务处理标识：	可以理解为报文的序列号，一般每次通信之后就要加1以区别不同的通信数据报文
 * 协议标识符：	00 00表示ModbusTCP协议
 * 长度：	    表示接下来的数据长度，单位为字节
 * 从机地址：	从机设备地址
 *
 * 功能码 含义	   功能码	    含义
 * 0x01	读线圈	    0x04	读输入寄存器
 * 0x05	写单个线圈	0x03	读保持寄存器
 * 0x0F	写多个线圈	0x06	写单个保持寄存器
 * 0x02	读离散量输入	0x10	写多个保持寄存器
 *
 * 详细资料：{@code https://www.cnblogs.com/ioufev/articles/10830028.html}
 * @see com.dream.iot.modbus.consts.ModbusCode
 */
public interface ModbusTcpMessage extends Message {

    @Override
    ModbusTcpBody getBody();

    @Override
    ModbusTcpHeader getHead();

    String getEquipCode();

    void setBody(MessageBody body);

    void setHead(MessageHead header);

    void setMessage(byte[] message);

    AttributeKey NEXT_KEY = AttributeKey.valueOf("Modbus:NextId");
}
