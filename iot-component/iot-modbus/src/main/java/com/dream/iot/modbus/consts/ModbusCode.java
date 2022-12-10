package com.dream.iot.modbus.consts;

import com.dream.iot.ProtocolType;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientMessage;

/**
 * modbus 功能码
 * 线圈寄存器：实际上就可以类比为开关量（继电器状态），以bit对应一个信号的开关状态。所以一个byte就可以同时控制8路的信号。
 *      比如控制外部8路io的高低线圈寄存器支持读也支持写，写在功能码里面又分为写单个线圈寄存器和写多个线圈寄存器。对应上面的功能码也就是：0x01  0x05  0x0f
 * 离散输入寄存器：如果线圈寄存器理解了这个自然也明白了。离散输入寄存器就相当于线圈寄存器的只读模式，他也是每个bit表示一个开关量，而他的开关量只能读取输入的开关信号，是不能够写的。
 *      比如我读取外部按键的按下还是松开。所以功能码也简单就一个读的 0x02
 * 保持寄存器：这个寄存器的单位不再是bit而是两个byte，也就是可以存放具体的数据量的，并且是可读写的。一般对应参数设置，
 *      比如我我设置时间年月日，不但可以写也可以读出来现在的时间。写也分为单个写和多个写，所以功能码有对应的三个：0x03 0x06 0x10
 * 输入寄存器：这个和保持寄存器类似，但是也是只支持读而不能写，一般是读取各种实时数据。一个寄存器也是占据两个byte的空间。类比我我通过读取输入寄存器获取现在的AD采集值。对应的功能码也就一个 0x04
 * @see ModbusTcpClientMessage
 */
public enum ModbusCode implements ProtocolType {
    Read01((byte) 0x01), // 读线圈(读写位模式)
    Read02((byte) 0x02), // 读离散量输入(位只读模式)
    Read03((byte) 0x03), // 读保持寄存器(字节读写模式)
    Read04((byte) 0x04), // 读输入寄存器(字节只读模式)

    Write05((byte) 0x05), // 写单个线圈(读写位模式)
    Write06((byte) 0x06), // 写单个保持寄存器
    Write0F((byte) 0x0F), // 写多个线圈
    Write10((byte) 0x10) // 写多个保持寄存器
    ;

    private byte code;

    ModbusCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ModbusCode INSTANCE(byte code) {
        switch (code) {
            case 0x01: return Read01;
            case 0x02: return Read02;
            case 0x03: return Read03;
            case 0x04: return Read04;

            case 0x05: return Write05;
            case 0x06: return Write06;
            case 0x0F: return Write0F;
            case 0x10: return Write10;

            default: throw new IllegalStateException("不存在功能码["+code+"]");
        }
    }

    @Override
    public Enum getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return this.code + "";
    }
}
