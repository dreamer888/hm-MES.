package com.dream.iot.plc;

import java.util.List;
import java.util.function.Function;

public interface PlcReadWrite {

    /**
     * 数据转换
     * @return
     */
    DataTransfer getDataTransfer();

    /**
     * 使用完整的报文写数据到plc
     * @param fullMessage
     */
    void writeFull(byte[] fullMessage);

    /**
     * 使用完整的报文读取数据
     * @param fullMessage
     * @return 返回要读取的数据
     */
    byte[] readFull(byte[] fullMessage);

    /**
     * 批量读取字节数组信息，需要指定地址和长度，返回原始的字节数组<br />
     * @param address 数据地址
     * @param length 数据长度
     */
    byte[] read(String address, short length);

    /**
     * 批量读取地位信息
     * @param batchAddress
     */
    List<byte[]> batchRead(List<ReadAddress> batchAddress);

    /**
     * 读取单个的bool数据信息<br />
     * @param address 数据地址
     */
    Boolean readBool(String address);

    /**
     * 读取16位的有符号的整型数据<br />
     * @param address 起始地址
     */
    Short readInt16(String address);

    /**
     * 读取16位的有符号整型数组<br />
     * @param address 起始地址
     * @param length 读取的数组长度
     */
    short[] readInt16(String address, short length);

    /**
     * 读取16位的无符号整型<br />
     * @param address 起始地址
     */
    Integer readUInt16(String address);

    /**
     * 读取16位的无符号整型数组<br />
     * @param address 起始地址
     * @param length 读取的数组长度
     */
    int[] readUInt16(String address, short length);

    /**
     * 读取32位的有符号整型<br />
     * @param address 起始地址
     */
    Integer readInt32(String address);

    /**
     * 读取32位有符号整型数组<br />
     * @param address 起始地址
     * @param length 数组长度
     */
    int[] readInt32(String address, short length);

    /**
     * 读取32位的无符号整型<br />
     * @param address 起始地址
     */
    Long readUInt32(String address);

    /**
     * 读取32位的无符号整型数组<br />
     * @param address 起始地址
     * @param length 数组长度
     */
    long[] readUInt32(String address, short length);

    /**
     * 读取64位的有符号整型<br />
     * @param address 起始地址
     */
    Long readInt64(String address);

    /**
     * 读取64位的有符号整型数组<br />
     * @param address 起始地址
     * @param length 数组长度
     */
    long[] readInt64(String address, short length);

    /**
     * 读取单浮点数据<br />
     * @param address 起始地址
     */
    Float readFloat(String address);

    /**
     * 读取单浮点精度的数组<br />
     * @param address 起始地址
     * @param length 数组长度
     */
    float[] readFloat(String address, short length);

    /**
     * 读取双浮点的数据<br />
     * @param address 起始地址
     */
    Double readDouble(String address);

    /**
     * 读取双浮点数据的数组<br />
     * @param address 起始地址
     * @param length 数组长度
     */
    double[] readDouble(String address, short length);

    /**
     * 读取字符串数据，默认为最常见的ASCII编码<br />
     * @param address 起始地址
     * @param length 数据长度
     */
    String readString(String address, short length);

    /**
     * 使用指定的编码，读取字符串数据<br />
     * @param address 起始地址
     * @param length 数据长度
     * @param encoding 指定的自定义的编码
     */
    String readString(String address, short length, String encoding);

    /**
     * 自定义要读取的数据
     * @param address 其实地址
     * @param length 读取的长度
     * @param function 处理函数
     * @param <R> 返回值
     */
    <R> R read(String address, short length, Function<byte[], R> function);

    /**
     * 写入原始的byte数组数据到指定的地址，返回是否写入成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, byte[] value);

    /**
     * 批量写入 {@link Boolean} 数组数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 要写入的数据 长度为8的倍数
     */
    void write(String address, boolean[] value);

    /**
     * 写入单个的 {@link Boolean} 数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, boolean value);

    /**
     * 写入short数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, short value);

    /**
     * 写入short数组，返回是否成功<br />
     * @param address 起始地址
     * @param values 写入值
     */
    void write(String address, short[] values);

    /**
     * 写入int数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, int value);

    /**
     * 写入int[]数组，返回是否成功<br />
     * @param address 起始地址
     * @param values 写入值
     */
    void write(String address, int[] values);

    /**
     * 写入long数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, long value);

    /**
     * 写入long数组，返回是否成功<br />
     * @param address 起始地址
     * @param values 写入值
     */
    void write(String address, long[] values);

    /**
     * 写入float数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, float value);

    /**
     * 写入float数组，返回是否成功<br />
     * @param address 起始地址
     * @param values 写入值
     */
    void write(String address, float[] values);

    /**
     * 写入double数据，返回是否成功<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, double value);

    /**
     * 写入double数组，返回是否成功<br />
     * @param address 起始地址
     * @param values 写入值
     */
    void write(String address, double[] values);

    /**
     * 写入字符串信息，编码为ASCII<br />
     * @param address 起始地址
     * @param value 写入值
     */
    void write(String address, String value);

    /**
     * 写入字符串信息，需要指定的编码信息<br />
     * @param address 起始地址
     * @param value 写入值
     * @param encoding 指定的编码信息
     */
    void write(String address, String value, String encoding);

}
