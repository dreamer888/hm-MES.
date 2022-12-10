package com.dream.iot.server.dtu;

import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.component.LengthFieldBasedFrameDecoderServerComponent;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteOrder;

/**
 * 使用长度字段解码器协议并且使用Dtu连网
 * @see DtuMessageDecoder
 * @see DtuFirstDeviceSnPackageHandler
 * @see LengthFieldBasedFrameDecoderServerComponent
 * @param <M>
 */
public abstract class LengthFieldBasedFrameForDtuDecoderServerComponent<M extends ServerMessage> extends LengthFieldBasedFrameDecoderServerComponent<M> implements DtuMessageDecoder<M> {

    private DtuMessageAware<M> dtuMessageAware = new DefaultDtuMessageAware<>(this);

    public LengthFieldBasedFrameForDtuDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(connectProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public LengthFieldBasedFrameForDtuDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(connectProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public LengthFieldBasedFrameForDtuDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(connectProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public LengthFieldBasedFrameForDtuDecoderServerComponent(ConnectProperties connectProperties, ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(connectProperties, byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return new LengthFieldBasedFrameMessageDecoderAdapter(getByteOrder(), getMaxFrameLength(), getLengthFieldOffset()
                , getLengthFieldLength(), getLengthAdjustment(), getInitialBytesToStrip(), isFailFast());
    }

    public DtuMessageAware<M> getDtuMessageAwareDelegation() {
        return this.dtuMessageAware;
    }

    public void setDtuMessageAware(DtuMessageAware<M> dtuMessageAware) {
        this.dtuMessageAware = dtuMessageAware;
        if(this.dtuMessageAware instanceof DefaultDtuMessageAware) {
            if(((DefaultDtuMessageAware<M>) this.dtuMessageAware).getDecoder() == null) {
                ((DefaultDtuMessageAware<M>) this.dtuMessageAware).setDecoder(this);
            }
        }
    }
}
