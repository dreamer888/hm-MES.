package com.dream.iot.plc.siemens;

import com.dream.iot.plc.ReverseDataTransfer;

/**
 * 西门子数据转换格式
 */
public class SiemensDataTransfer extends ReverseDataTransfer {

    private static SiemensDataTransfer dataTransfer = new SiemensDataTransfer();

    protected SiemensDataTransfer() { }

    public static SiemensDataTransfer getInstance() {
        return dataTransfer;
    }

    @Override
    public byte[] byte4Transform(byte[] data, int offset) {
        return data;
    }

    @Override
    public byte[] byte8Transform(byte[] data, int offset) {
        return data;
    }
}
