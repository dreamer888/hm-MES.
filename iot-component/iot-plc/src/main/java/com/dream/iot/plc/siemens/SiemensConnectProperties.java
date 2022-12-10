package com.dream.iot.plc.siemens;

import com.dream.iot.client.ClientConnectProperties;

public class SiemensConnectProperties extends ClientConnectProperties {

    /**
     * 槽号
     */
    private byte slot;

    /**
     * 机架号  等同于shelf
     */
    private byte rack;//等同于shelf

    /**
     * 西门子型号
     */
    private SiemensModel model;

    public SiemensConnectProperties(String remoteHost) {
        this(remoteHost, SiemensModel.S1200);
    }

    public SiemensConnectProperties(String remoteHost, byte slot, byte rack) {
        this(remoteHost, 102, SiemensModel.S1200, slot, rack);
    }

    public SiemensConnectProperties(String remoteHost, SiemensModel model) {
        this(remoteHost, 102, model);
    }

    public SiemensConnectProperties(String remoteHost, int remotePort, SiemensModel model) {
        this(remoteHost, remotePort, model, (byte) 0, (byte) 0);
    }

    public SiemensConnectProperties(String remoteHost, Integer remotePort, SiemensModel model, String connectKey) {
        super(remoteHost, remotePort, connectKey);
        this.model = model;
    }

    public SiemensConnectProperties(String remoteHost, int remotePort, SiemensModel model, byte slot, byte rack) {
        super(remoteHost, remotePort, remoteHost + ":" + remotePort + ":" + model + ":" + rack + "_" + slot);
        this.slot = slot;
        this.rack = rack;
        this.model = model;
    }

    public SiemensModel getModel() {
        return model;
    }

    public void setModel(SiemensModel model) {
        this.model = model;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public byte getRack() {
        return rack;
    }

    public void setRack(byte rack) {
        this.rack = rack;
    }
}
