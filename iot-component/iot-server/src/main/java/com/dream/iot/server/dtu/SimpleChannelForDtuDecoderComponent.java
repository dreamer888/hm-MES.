package com.dream.iot.server.dtu;

import com.dream.iot.codec.adapter.SimpleChannelDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.component.SimpleChannelDecoderServerComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Dtu 简单通道解码器
 * @see DtuMessageDecoder
 * @see DtuFirstDeviceSnPackageHandler dtu的第一包上报设备编号
 */
public abstract class SimpleChannelForDtuDecoderComponent<M extends ServerMessage> extends SimpleChannelDecoderServerComponent<M> implements DtuMessageDecoder<M> {

    private DtuMessageAware<M> dtuMessageAwareDelegation;

    public SimpleChannelForDtuDecoderComponent(ConnectProperties connectProperties) {
        super(connectProperties);
        this.dtuMessageAwareDelegation = new DefaultDtuMessageAware<>(this);
    }

    public SimpleChannelForDtuDecoderComponent(ConnectProperties connectProperties, DtuMessageAware<M> dtuMessageAwareDelegation) {
        super(connectProperties);
        this.setDtuMessageAwareDelegation(dtuMessageAwareDelegation);
    }

    @Override
    public SimpleChannelDecoderAdapter getMessageDecoder() {
        /**
         * 此处必须禁用自动释放功能
         * @see #doTcpDecode(ChannelHandlerContext, ByteBuf) 此处已经 release
         */
        return new SimpleChannelDecoderAdapter(false, this);
    }

    @Override
    public DtuMessageAware<M> getDtuMessageAwareDelegation() {
        return dtuMessageAwareDelegation;
    }

    public void setDtuMessageAwareDelegation(DtuMessageAware<M> dtuMessageAwareDelegation) {
        this.dtuMessageAwareDelegation = dtuMessageAwareDelegation;
        if(this.dtuMessageAwareDelegation instanceof DefaultDtuMessageAware) {
            if(((DefaultDtuMessageAware<M>) this.dtuMessageAwareDelegation).getDecoder() == null) {
                ((DefaultDtuMessageAware<M>) this.dtuMessageAwareDelegation).setDecoder(this);
            }
        }
    }
}
