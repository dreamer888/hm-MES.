package com.dream.iot.client.component;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.codec.LengthFieldBasedFrameClient;

import java.nio.ByteOrder;

public abstract class LengthFieldBasedFrameClientComponent<M extends ClientMessage> extends TcpClientComponent<M> {

    private ByteOrder byteOrder;
    private int maxFrameLength;
    private int lengthFieldOffset;
    private int lengthFieldLength;
    private int lengthAdjustment;
    private int initialBytesToStrip;
    private boolean failFast;

    public LengthFieldBasedFrameClientComponent(ClientConnectProperties config, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        this(config, maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
    }

    public LengthFieldBasedFrameClientComponent(ClientConnectProperties config, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        this(config, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
    }

    public LengthFieldBasedFrameClientComponent(ClientConnectProperties config, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        this(config, ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public LengthFieldBasedFrameClientComponent(ClientConnectProperties config, ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(config);
        this.failFast = failFast;
        this.byteOrder = byteOrder;

        this.maxFrameLength = maxFrameLength;
        this.lengthAdjustment = lengthAdjustment;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;

        this.initialBytesToStrip = initialBytesToStrip;
    }

    public LengthFieldBasedFrameClientComponent(ClientConnectProperties config, MultiClientManager clientManager, ByteOrder byteOrder, int maxFrameLength
            , int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(config, clientManager);
        this.failFast = failFast;
        this.byteOrder = byteOrder;

        this.maxFrameLength = maxFrameLength;
        this.lengthAdjustment = lengthAdjustment;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;

        this.initialBytesToStrip = initialBytesToStrip;
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new LengthFieldBasedFrameClient(this, config, byteOrder, maxFrameLength
                , lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

}
