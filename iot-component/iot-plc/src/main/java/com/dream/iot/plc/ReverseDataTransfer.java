package com.dream.iot.plc;

import com.dream.iot.utils.ByteUtil;

/**
 * 倒序的数据
 */
public abstract class ReverseDataTransfer implements DataTransfer {

    @Override
    public short toShort(byte[] bytes, int offset) {
        return ByteUtil.bytesToShortOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromShort(short value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public int toUShort(byte[] bytes, int offset) {
        return ByteUtil.bytesToUShortOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromUShort(short value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public int toInt(byte[] bytes, int offset) {
        return ByteUtil.bytesToIntOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromInt(int value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public long toUInt(byte[] bytes, int offset) {
        return ByteUtil.bytesToUIntOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromUInt(int value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public long toLong(byte[] bytes, int offset) {
        return ByteUtil.bytesToLongOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromLong(long value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public float toFloat(byte[] bytes, int offset) {
        return ByteUtil.bytesToFloatOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromFloat(float value) {
        return ByteUtil.getBytesOfReverse(value);
    }

    @Override
    public double toDouble(byte[] bytes, int offset) {
        return ByteUtil.bytesToDoubleOfReverse(bytes, offset);
    }

    @Override
    public byte[] fromDouble(double value) {
        return ByteUtil.getBytesOfReverse(value);
    }
}
