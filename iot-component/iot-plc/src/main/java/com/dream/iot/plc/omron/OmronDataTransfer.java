package com.dream.iot.plc.omron;

import com.dream.iot.plc.DefaultDataTransfer;

/**
 * 欧姆龙数据转换
 */
public class OmronDataTransfer extends DefaultDataTransfer {

    private static OmronDataTransfer dataTransfer = new OmronDataTransfer();

    protected OmronDataTransfer() { }

    public static OmronDataTransfer getInstance() {
        return dataTransfer;
    }

    @Override
    public byte[] byte4Transform(byte[] data, int offset) {
        byte[] buffer = new byte[4];
        buffer[0] = data[offset + 1];
        buffer[1] = data[offset + 0];
        buffer[2] = data[offset + 3];
        buffer[3] = data[offset + 2];
        return buffer;
    }

    @Override
    public byte[] byte8Transform(byte[] data, int offset) {
        byte[] buffer = new byte[8];
        buffer[0] = data[offset + 1];
        buffer[1] = data[offset + 0];
        buffer[2] = data[offset + 3];
        buffer[3] = data[offset + 2];
        buffer[4] = data[offset + 5];
        buffer[5] = data[offset + 4];
        buffer[6] = data[offset + 7];
        buffer[7] = data[offset + 6];
        return buffer;
    }
}
