package com.dream.iot.plc.siemens;

import com.dream.iot.Message;
import com.dream.iot.plc.ReadAddress;
import com.dream.iot.plc.WriteAddress;

import java.util.List;

public class SiemensMessageBody implements Message.MessageBody {

    private byte[] message;

    protected SiemensMessageBody(byte[] message) {
        this.message = message;
    }

    public static SiemensMessageBody buildReadBody(List<ReadAddress> batchAddress) {
        byte[] message = new byte[batchAddress.size() * 12];

        for(int i = 0; i < batchAddress.size(); i++) {
            ReadAddress address = batchAddress.get(i);
            int[] analysis = SiemensUtils.analysisAddress(address.getAddress());

            // 指定有效值类型
            message[0 + i * 12] = 0x12;
            // 接下来本次地址访问长度
            message[1 + i * 12] = 0x0A;
            // 语法标记 ANY
            message[2 + i * 12] = 0x10;
            // 1. 是按位 2. 按字为单位
            message[3 + i * 12] = address.getType().getType();

            // 访问数据的个数
            message[4 + i * 12] = (byte) (address.getLength() / 256);
            message[5 + i * 12] = (byte) (address.getLength() % 256);

            // 访问的是DB块
            message[6 + i * 12] = (byte) (analysis[2] / 256);
            message[7 + i * 12] = (byte) (analysis[2] % 256);

            // 访问数据类型
            message[8 + i * 12] = (byte) analysis[0];

            // 偏移位置
            message[9 + i * 12] = (byte) (analysis[1] / 256 / 256 % 256);
            message[10 + i * 12] = (byte) (analysis[1] / 256 % 256);
            message[11 + i * 12] = (byte) (analysis[1] % 256);
        }

        return new SiemensMessageBody(message);
    }

    public static SiemensMessageBody buildWriteBody(WriteAddress address) {
        int length = address.getData().length;
        int[] analysis = SiemensUtils.analysisAddress(address.getAddress());

        byte[] message = new byte[16 + length];

        // 固定，返回数据长度
        message[0] = 0x12;
        message[1] = 0x0A;
        message[2] = 0x10;

        // 写入方式，1是按位，2是按字
        message[3] = address.getType().getType();

        // 写入数据的个数
        message[4] = (byte) (length / 256);
        message[5] = (byte) (length % 256);

        // 访问的是DB块
        message[6] = (byte) (analysis[2] / 256);
        message[7] = (byte) (analysis[2] % 256);

        // 写入数据的类型
        message[8] = (byte) analysis[0];

        // 偏移位置
        message[9] = (byte) (analysis[1] / 256 / 256 % 256);
        message[10] = (byte) (analysis[1] / 256 % 256);
        message[11] = (byte) (analysis[1] % 256);

        // 写入方式
        message[12] = 0x00;
        message[13] = (byte) (address.getType() == AddressType.Word ? 0x04 : 0x03);

        // 计算的长度
        message[14] = (byte) (address.getType() == AddressType.Word ? length * 8 / 256 : length / 256);
        message[15] = (byte) (address.getType() == AddressType.Word ? length * 8 % 256 : length % 256);

        System.arraycopy(address.getData(), 0, message, 16, length);
        return new SiemensMessageBody(message);
    }

    @Override
    public byte[] getMessage() {
        return message;
    }
}
