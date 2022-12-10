package com.dream.iot.modbus;

import com.dream.iot.utils.ByteUtil;

import java.nio.charset.Charset;

public class ReadPayload extends Payload{

    private int start;

    public ReadPayload(byte[] payload, int start) {
        super(payload);
        this.start = start;
    }

    public short readShort(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToShortOfReverse(this.getPayload(), offset);
    }


    public int readUShort(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToUShortOfReverse(this.getPayload(), offset);
    }

    public int readInt(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToInt(this.getPayload(), offset);
    }

    public long readUInt(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToUInt(this.getPayload(), offset);
    }

    public long readLong(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToLong(this.getPayload(), offset);
    }

    @Override
    public float readFloat(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToFloat(this.getPayload(), offset);
    }

    @Override
    public double readDouble(int start) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToDouble(this.getPayload(), offset);
    }

    @Override
    public String readString(int start, int num) {
        validatorStartAddress(start);
        int offset = (start - this.start) * 2;
        return ByteUtil.bytesToString(this.getPayload(), offset, offset + num * 2, Charset.forName("UTF-8"));
    }

    private void validatorStartAddress(int start) {
        if(start < this.start) {
            throw new IllegalArgumentException("读取的寄存器地址不能小于["+this.start+"]");
        }
    }
}
