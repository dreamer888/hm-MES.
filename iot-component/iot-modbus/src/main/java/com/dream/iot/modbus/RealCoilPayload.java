package com.dream.iot.modbus;

import com.dream.iot.modbus.consts.ModbusBitStatus;
import com.dream.iot.utils.ByteUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 读线圈负载
 */
public class RealCoilPayload extends Payload {

    /**
     * 读几个线圈寄存器
     */
    private int num;

    /**
     * 每个线圈(二进制位)的状态 0.OFF  1.ON
     */
    private List<ModbusBitStatus> statuses;

    public RealCoilPayload(int num, byte[] payload) {
        super(payload);
        this.num = num;
        this.statuses = new ArrayList<>();
        this.buildBitStatus(payload, this.statuses);
    }

    /**
     * 获取指定位的boolean状态
     * @param bit
     * @return  1. true 0. false
     */
    public boolean readBoolean(int bit) {
        return this.getStatuses().get(bit) == ModbusBitStatus.ON;
    }

    /**
     * 读取指定位的状态
     * @param bit
     * @return
     */
    public ModbusBitStatus readStatus(int bit) {
        return this.getStatuses().get(bit);
    }

    public List<ModbusBitStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<ModbusBitStatus> statuses) {
        this.statuses = statuses;
    }

    private void buildBitStatus(byte[] bits, List<ModbusBitStatus> statuses) {
        for(int j=0; j<bits.length; j++) {
            byte item = bits[j];
            for(byte i=0; i<8; i++) {
                int num = j * 8 + i + 1;
                if(num > this.num) return;

                final byte bit = ByteUtil.bitAtByte((byte) (item & 0xFF), i);
                statuses.add(bit == 0 ? ModbusBitStatus.OFF : ModbusBitStatus.ON);
            }
        }
    }
}
