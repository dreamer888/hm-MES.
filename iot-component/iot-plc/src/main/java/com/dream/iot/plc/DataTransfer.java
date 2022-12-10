package com.dream.iot.plc;

public interface DataTransfer {

    short toShort(byte[] bytes, int offset);

    byte[] fromShort(short value);

    int toUShort(byte[] bytes, int offset);

    byte[] fromUShort(short value);

    int toInt(byte[] bytes, int offset);

    byte[] fromInt(int value);

    long toUInt(byte[] bytes, int offset);

    byte[] fromUInt(int value);

    long toLong(byte[] bytes, int offset);

    byte[] fromLong(long value);

    float toFloat(byte[] bytes, int offset);

    byte[] fromFloat(float value);

    double toDouble(byte[] bytes, int offset);

    byte[] fromDouble(double value);

    /**
     * 四个字节的转换
     * @param data
     * @param offset
     * @return
     */
    byte[] byte4Transform(byte[] data, int offset);

    /**
     * 8个字节的转换
     * @param data
     * @param offset
     * @return
     */
    byte[] byte8Transform(byte[] data, int offset);
}
