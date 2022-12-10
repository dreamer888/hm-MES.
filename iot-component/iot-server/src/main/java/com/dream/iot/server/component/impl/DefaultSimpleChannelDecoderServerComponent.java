package com.dream.iot.server.component.impl;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.SimpleChannelDecoderServerComponent;

public class DefaultSimpleChannelDecoderServerComponent extends SimpleChannelDecoderServerComponent<DefaultSimpleServerMessage> {

    private String desc;
    private String name;

    public DefaultSimpleChannelDecoderServerComponent(ConnectProperties connectProperties) {
        this(connectProperties, "SimpleDecoder", "SimpleChannelDecoderAdapter默认实现");
    }

    public DefaultSimpleChannelDecoderServerComponent(ConnectProperties connectProperties, String name, String desc) {
        super(connectProperties);
        this.desc = desc;
        this.name = name;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public AbstractProtocol getProtocol(DefaultSimpleServerMessage message) {
        return null;
    }

    @Override
    public DefaultSimpleServerMessage createMessage(byte[] message) {
        return new DefaultSimpleServerMessage(message);
    }
}
