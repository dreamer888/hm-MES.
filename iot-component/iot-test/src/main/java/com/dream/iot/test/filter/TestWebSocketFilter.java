package com.dream.iot.test.filter;

import com.dream.iot.CoreConst;
import com.dream.iot.Message;
import com.dream.iot.codec.filter.RegisterParams;
import com.dream.iot.server.websocket.impl.DefaultWebSocketServerComponent;
import com.dream.iot.websocket.HttpRequestWrapper;
import com.dream.iot.websocket.WebSocketFilter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.stereotype.Component;

@Component
public class TestWebSocketFilter implements WebSocketFilter<DefaultWebSocketServerComponent> {

    @Override
    public boolean isActivation(Channel channel, DefaultWebSocketServerComponent component) {
        return WebSocketFilter.super.isActivation(channel, component);
    }

    @Override
    public Message.MessageHead register(Message.MessageHead head, RegisterParams params) {
        HttpRequestWrapper httpRequestWrapper = params.getValue(CoreConst.WEBSOCKET_REQ);
        httpRequestWrapper.getQueryParam("client").ifPresent(deviceSn -> head.setEquipCode(deviceSn));

        return head;
    }

    @Override
    public HttpResponseStatus authentication(HttpRequest request) {
        return HttpResponseStatus.OK;
    }
}
