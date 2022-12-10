package com.dream.iot.server.udp.impl;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.DatagramPacketDecoderServerComponent;

public class DefaultUdpServerComponent extends DatagramPacketDecoderServerComponent<DefaultUdpServerMessage> {

    public DefaultUdpServerComponent(ConnectProperties config) {
        super(config);
    }

    @Override
    public String getDesc() {
        return "UDP协议IOT默认实现";
    }

    @Override
    public AbstractProtocol getProtocol(DefaultUdpServerMessage message) {
        return new DefaultUdpServerProtocol(message);
    }

    @Override
    public String getName() {
        return "UDP(默认)";
    }

    @Override
    public Class<DefaultUdpServerMessage> getMessageClass() {
        return DefaultUdpServerMessage.class;
    }

    @Override
    public DefaultUdpServerMessage createMessage(byte[] message) {
        return new DefaultUdpServerMessage(message);
    }
}
