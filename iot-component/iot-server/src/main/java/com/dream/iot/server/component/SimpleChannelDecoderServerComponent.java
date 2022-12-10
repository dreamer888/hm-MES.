package com.dream.iot.server.component;

import com.dream.iot.codec.adapter.SimpleChannelDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.ServerMessage;

public abstract class SimpleChannelDecoderServerComponent<M extends ServerMessage> extends TcpDecoderServerComponent<M> {

    public SimpleChannelDecoderServerComponent(ConnectProperties connectProperties) {
        super(connectProperties);
    }

    @Override
    public SimpleChannelDecoderAdapter getMessageDecoder() {
        return new SimpleChannelDecoderAdapter(this);
    }

}
