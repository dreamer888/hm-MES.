package com.dream.iot.test.client.line;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.component.SingleTcpClientComponent;
import com.dream.iot.codec.adapter.LineBasedFrameMessageDecoderAdapter;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.line.LineMessageHead;
import io.netty.channel.ChannelInboundHandler;

public class LineClientComponent extends SingleTcpClientComponent<LineClientMessage> {

    public LineClientComponent(ClientConnectProperties config) {
        super(config);
    }

    @Override
    public String getName() {
        return "换行符解码";
    }

    @Override
    public AbstractProtocol getProtocol(LineClientMessage message) {
        LineMessageHead head = message.getHead();
        if(head.getType() == TestProtocolType.CIReq) {
            return remove(message.getMessageId());
        } else if(head.getType() == TestProtocolType.PIReq) {
            return new LineServerInitiativeProtocol(message);
        } else {
            return null;
        }
    }

    @Override
    public Class<LineClientMessage> getMessageClass() {
        return LineClientMessage.class;
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new LineBasedFrameMessageDecoderAdapter(1024);
    }
}
