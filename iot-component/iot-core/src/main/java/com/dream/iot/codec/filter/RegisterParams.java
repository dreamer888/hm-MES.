package com.dream.iot.codec.filter;

import com.dream.iot.FrameworkComponent;
import com.dream.iot.message.UnParseBodyMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class RegisterParams {

    private Channel channel;

    private UnParseBodyMessage message;

    private FrameworkComponent component;

    public RegisterParams(UnParseBodyMessage message, Channel channel, FrameworkComponent component) {
        this.channel = channel;
        this.message = message;
        this.component = component;
    }

    public <T> Attribute<T> getAttr(String name) {
        return channel.attr(AttributeKey.valueOf(name));
    }

    public <P> P getValue(AttributeKey<P> attr) {
        return channel.attr(attr).get();
    }

    /**
     * 关闭连接
     * @return
     */
    public ChannelFuture close() {
        return this.channel.close();
    }

    /**
     * 写出错误报文
     * @param errMsg
     * @return
     */
    public ChannelFuture writeAndFlush(byte[] errMsg) {
        return this.channel.writeAndFlush(Unpooled.wrappedBuffer(errMsg));
    }

    public <C extends FrameworkComponent> C getComponent() {
        return (C) component;
    }

    public UnParseBodyMessage getMessage() {
        return message;
    }
}
