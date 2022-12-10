package com.dream.iot.plc.omron;

public enum OmronFinsModel {

    /**
     * DM Area
     */
    DM((byte) 0x02, (byte) 0x82),

    /**
     * CIO Area
     */
    CIO((byte)0x30, (byte)0xB0),

    /**
     * Work Area
     */
    WR((byte)0x31, (byte)0xB1),

    /**
     * Holding Bit Area
     */
    HR((byte)0x32,  (byte)0xB2),

    /**
     * Auxiliary Bit Area
     */
    AR((byte)0x33,  (byte)0xB3),

    /**
     * Tim Or CNT Area
     */
    TIM((byte)0x09, (byte) 0x89)
    ;

    private byte bit = 0;
    private byte word = 0;

    OmronFinsModel(byte bit, byte word) {
        this.bit = bit;
        this.word = word;
    }

    public byte getBit() {
        return bit;
    }

    public byte getWord() {
        return word;
    }
}
