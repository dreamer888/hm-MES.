package com.dream.iot.modbus;

import com.dream.iot.modbus.consts.ModbusBitStatus;

public abstract class Payload {

    private byte[] payload;

    public Payload(byte[] payload) {
        this.payload = payload;
    }

    /**
     * 获取指定位的boolean状态
     * @param bit 从0开始
     * @return  1. true 0. false
     */
    public boolean readBoolean(int bit) {
        throw new UnsupportedOperationException("不支持操作");
    }

    /**
     * 读取指定位的状态
     * @param bit 从0开始
     * @return
     */
    public ModbusBitStatus readStatus(int bit) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public short readShort(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public int readUShort(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public int readInt(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public long readUInt(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public long readLong(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public float readFloat(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public double readDouble(int start) {
        throw new UnsupportedOperationException("不支持操作");
    }

    /**
     * 使用UTF-8编码读取
     * @param start 从哪个地址开始读
     * @param num 读取几个地址 (一个地址两个字节)
     * @return
     */
    public String readString(int start, int num) {
        throw new UnsupportedOperationException("不支持操作");
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
