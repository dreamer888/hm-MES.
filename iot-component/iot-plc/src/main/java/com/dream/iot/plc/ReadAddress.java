package com.dream.iot.plc;

import com.dream.iot.plc.siemens.AddressType;

public class ReadAddress {

    /**
     * 读取的长度(字节)
     */
    private short length;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否按位读取
     */
    private AddressType type;

    protected ReadAddress(String address, short length, AddressType type) {
        this.type = type;
        this.length = length;
        this.address = address;
    }

    /**
     * @param address M100, I100, Q100, DB1.100
     * @return
     */
    public static ReadAddress buildBitRead(String address) {
        return new ReadAddress(address, (short) 1, AddressType.Bit);
    }

    /**
     * 以字节为读取单位
     * @param address M100, I100, Q100, DB1.100
     * @param length 读取的长度
     * @return
     */
    public static ReadAddress buildByteRead(String address, short length) {
        return new ReadAddress(address, length, AddressType.Word);
    }

    public short getLength() {
        if(this.type == AddressType.Bit) {
            return 1;
        } else {
            return length;
        }
    }

    public void setLength(short length) {
        this.length = length;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressType getType() {
        return type;
    }
}
