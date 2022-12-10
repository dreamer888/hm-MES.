package com.dream.iot.plc;

import com.dream.iot.plc.siemens.AddressType;

public class WriteAddress {

    /**
     * 要写数据
     */
    private byte[] data;

    /**
     * 要写到的地址
     */
    private String address;

    /**
     * 写类型
     */
    private AddressType type;

    public WriteAddress(byte[] data, String address) {
        this(data, address, AddressType.Word);
    }

    public WriteAddress(byte[] data, String address, AddressType type) {
        this.data = data;
        this.type = type;
        this.address = address;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    public void setType(AddressType type) {
        this.type = type;
    }
}
