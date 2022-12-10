package com.dream.iot.plc;

import com.dream.iot.utils.ByteUtil;

public abstract class DefaultDataTransfer implements DataTransfer {

    protected DefaultDataTransfer() { }

    @Override
    public short toShort(byte[] bytes, int offset) {
        return ByteUtil.bytesToShort(bytes, offset);
    }

    @Override
    public byte[] fromShort(short value) {
        return ByteUtil.getBytes(value);
    }

    @Override
    public int toUShort(byte[] bytes, int offset) {
        return ByteUtil.bytesToUShort(bytes, offset);
    }

    @Override
    public byte[] fromUShort(short value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public int toInt(byte[] bytes, int offset) {
        return ByteUtil.bytesToInt(byte4Transform(bytes, offset), 0);
    }

    @Override
    public byte[] fromInt(int value) {
        return byte4Transform(ByteUtil.getBytes(value), 0);
    }

    @Override
    public long toUInt(byte[] bytes, int offset) {
        return ByteUtil.bytesToUInt(byte4Transform(bytes, offset), 0);
    }

    @Override
    public byte[] fromUInt(int value) {
        return byte4Transform(ByteUtil.getBytes(value), 0);
    }

    @Override
    public long toLong(byte[] bytes, int offset) {
        return ByteUtil.bytesToLong(byte8Transform(bytes, offset), 0);
    }

    @Override
    public byte[] fromLong(long value) {
        return byte8Transform(ByteUtil.getBytes(value), 0);
    }

    @Override
    public float toFloat(byte[] bytes, int offset) {
        return ByteUtil.bytesToFloat(byte4Transform(bytes, offset), 0);
    }

    @Override
    public byte[] fromFloat(float value) {
        return byte4Transform(ByteUtil.getBytes(value), 0);
    }

    @Override
    public double toDouble(byte[] bytes, int offset) {
        return ByteUtil.bytesToDouble(byte8Transform(bytes, offset), 0);
    }

    @Override
    public byte[] fromDouble(double value) {
        return byte8Transform(ByteUtil.getBytes(value), 0);
    }
}
