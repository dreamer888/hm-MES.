package com.dream.iot.client.codec;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.ByteToMessageDecoderAdapter;

public class ByteToMessageDecoderClient extends TcpSocketClient {

    public ByteToMessageDecoderClient(TcpClientComponent clientComponent, ClientConnectProperties config) {
        super(clientComponent, config);
    }

    @Override
    protected ByteToMessageDecoderAdapter createProtocolDecoder() {
        return new ByteToMessageDecoderAdapter(this.getClientComponent());
    }
}
