package com.dream.iot.test.server.websocket;

import com.dream.iot.server.websocket.impl.DefaultWebSocketServerProtocol;
import com.dream.iot.server.websocket.WebSocketChannelMatcher;
import com.dream.iot.server.websocket.WebSocketServerListener;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "iot.server", name = "websocket.start", havingValue = "true")
public class TestGroupServerListener implements WebSocketServerListener {

    private String client = "WEBSOCKET:IOT";

    @Override
    public String uri() {
        return "/group";
    }

    @Override
    public void onText(DefaultWebSocketServerProtocol protocol) {
        Optional<String> id = protocol.requestMessage().getQueryParam("group");
        // 过滤掉参数=WEBSOCKET:IOT的客户端
        DefaultWebSocketServerProtocol.writeGroup(uri(), id.get()
                , new WebSocketChannelMatcher(((channel, request) -> !(request.getQueryParam("client").get().equals(client)))))
                .get().addListener(future -> {
                    // 单独向客户端编号：WEBSOCKET:IOT 写数据测试
                    DefaultWebSocketServerProtocol.write(client, client);
                });
    }

    @Override
    public void onClose(DefaultWebSocketServerProtocol protocol) {
        // 关闭相同uri的一组客户端
        Optional<String> id = protocol.requestMessage().getQueryParam("group");

        // 过滤掉参数=WEBSOCKET:IOT的客户端
        DefaultWebSocketServerProtocol.closeGroup(uri(), WebSocketCloseStatus.NORMAL_CLOSURE, id.get()
                , new WebSocketChannelMatcher(((channel, request) -> !(request.getQueryParam("client").get().equals(client)))))
                .get().awaitUninterruptibly();
    }

    @Override
    public void onBinary(DefaultWebSocketServerProtocol protocol) {
        Optional<String> id = protocol.requestMessage().getQueryParam("group");

        // 过滤掉参数=WEBSOCKET:IOT的客户端
        DefaultWebSocketServerProtocol.writeGroup(uri(), id.get().getBytes(StandardCharsets.UTF_8)
                , new WebSocketChannelMatcher(((channel, request) -> !(request.getQueryParam("client").get().equals(client)))))
                .get().addListener(future -> {
                    // 单独向客户端编号：WEBSOCKET:IOT 写数据测试
                    DefaultWebSocketServerProtocol.write(client, client.getBytes(StandardCharsets.UTF_8));
                });

    }
}
