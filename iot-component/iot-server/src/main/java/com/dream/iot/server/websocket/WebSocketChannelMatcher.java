package com.dream.iot.server.websocket;

import com.dream.iot.CoreConst;
import com.dream.iot.websocket.HttpRequestWrapper;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelMatcher;

import java.util.function.BiFunction;

public class WebSocketChannelMatcher implements ChannelMatcher {

    private BiFunction<Channel, HttpRequestWrapper, Boolean> function;

    public WebSocketChannelMatcher(BiFunction<Channel, HttpRequestWrapper, Boolean> function) {
        this.function = function;
    }

    @Override
    public boolean matches(Channel channel) {
        return function.apply(channel, channel.attr(CoreConst.WEBSOCKET_REQ).get());
    }
}
