package com.dream.iot.client.websocket;

import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.codec.WebSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.websocket.WebSocketEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;

public abstract class WebSocketClientComponentAbstract<M extends WebSocketClientMessage> extends TcpClientComponent<M> implements WebSocketClientComponent<M>{

    private WebSocketEncoder webSocketEncoder = new WebSocketEncoder(this);

    public WebSocketClientComponentAbstract() { }

    public WebSocketClientComponentAbstract(WebSocketClientConnectProperties config) {
        super(config);
    }

    public WebSocketClientComponentAbstract(WebSocketClientConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    @Override
    public void init(Object... args) {
        super.init(args);
    }

    @Override
    public WebSocketClientHandshaker createClientHandShaker(WebSocketClient client) {
        WebSocketClientConnectProperties config = client.getConfig();
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory
                .newHandshaker(config.getUri(), config.getVersion(), config.getSubprotocol()
                        , config.isAllowExtensions(), config.getCustomHeaders(), config.getMaxFramePayloadLength()
                        , config.isPerformMasking(), config.isAllowMaskMismatch(), config.getForceCloseTimeoutMillis());
        return handshaker;
    }

    public WebSocketEncoder getWebSocketEncoder() {
        return webSocketEncoder;
    }

    public void setWebSocketEncoder(WebSocketEncoder webSocketEncoder) {
        this.webSocketEncoder = webSocketEncoder;
    }
}
