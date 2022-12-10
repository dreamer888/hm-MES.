package com.dream.iot.test.client.websocket;

import com.dream.iot.client.websocket.WebSocketClientConnectProperties;
import com.dream.iot.client.websocket.WebSocketClientListener;
import com.dream.iot.client.websocket.impl.DefaultWebSocketClientComponent;
import com.dream.iot.client.websocket.impl.DefaultWebSocketClientProtocol;
import com.dream.iot.server.IotServerProperties;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.websocket.WebSocketCloseBody;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Websocket group测试
 */
@Component
@ConditionalOnProperty(prefix = "iot.server", name = "websocket.start", havingValue = "true")
public class TestGroupClientListener implements WebSocketClientListener, InitializingBean, IotTestHandle {

    private int clientNum = 8;
    private String clientGroup = "GROUP";
    private String client = "WEBSOCKET:IOT";
    private String clientPrefix = "WEBSOCKET:";
    private Map<String, Integer> tempResult = new HashMap<>();
    @Autowired
    private IotTestProperties properties;
    @Autowired
    private IotServerProperties serverProperties;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private WebSocketClientConnectProperties clientConnectProperties;
    @Autowired
    private DefaultWebSocketClientComponent defaultWebSocketClientComponent;

    @Override
    public String uri() {
        IotServerProperties.WebSocketConnectProperties websocket = serverProperties.getWebsocket();
        clientConnectProperties = new WebSocketClientConnectProperties(String
                .format("ws://%s:%d/group?client=%s&group=%s", websocket.getHost(), websocket.getPort(), this.client, this.clientGroup));
        return clientConnectProperties.getUri().toString();
    }

    @Override
    public void onConnect(DefaultWebSocketClientProtocol protocol) {

    }

    @Override
    public synchronized void onText(DefaultWebSocketClientProtocol protocol) {
        URI uri = protocol.requestMessage().getProperties().getUri();
        if(protocol.getText().contains(clientGroup)) {
            Integer value = this.tempResult.get("text");
            this.tempResult.put("text", value + 1);
        }
        if(protocol.getText().equals(client)) {
            if(tempResult.get("text") == clientNum) {
                logger.info("websocket测试 文本测试 - 客户端数量：{} - 收到返回数量：{} - 状态：通过", clientNum, clientNum);
            } else {
                logger.error("websocket测试 文本测试 - 客户端数量：{} - 收到返回数量：{} - 状态：失败", clientNum, tempResult.get("text"));
            }
        }
    }

    @Override
    public synchronized void onClose(DefaultWebSocketClientProtocol protocol) {
        WebSocketCloseBody closeBody = protocol.getCloseBody();
        URI uri = protocol.requestMessage().getProperties().getUri();
        if(closeBody.getReasonText().contains(clientGroup)) {
            logger.info("组测试(websocket) 关闭测试 - uri：{} - 参数：{} - 状态：成功", uri, closeBody.getReasonText());
        } else {
            logger.error("组测试(websocket) 关闭测试 - uri：{} - 参数：{} - 状态：失败", uri, closeBody.getReasonText());
        }
    }

    @Override
    public synchronized void onBinary(DefaultWebSocketClientProtocol protocol) {
        URI uri = protocol.requestMessage().getProperties().getUri();
        String param = new String(protocol.getBinaryData(), StandardCharsets.UTF_8);
        if(param.contains(clientGroup)) {
            Integer value = this.tempResult.get("binary");
            this.tempResult.put("binary", value + 1);
        }

        if(param.equals(client)) {
            if(tempResult.get("binary") == clientNum) {
                logger.info("websocket测试 二进制测试 - 客户端数量：{} - 收到返回数量：{} - 状态：通过", clientNum, clientNum);
            } else {
                logger.error("websocket测试 二进制测试 - 客户端数量：{} - 收到返回数量：{} - 状态：失败", clientNum, tempResult.get("binary"));
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tempResult.put("text", 0);
        tempResult.put("close", 0);
        tempResult.put("binary", 0);
        tempResult.put("deviceSnBind", 0);
    }

    @Override
    public void start() throws Exception {
        System.out.println("--------------------------------------- 开始WebSocket测试 ------------------------------------------");

        // 创建客户端
        for (int i = 0; i < clientNum; i++) {
            String host = properties.getHost();
            IotServerProperties.WebSocketConnectProperties websocket = serverProperties.getWebsocket();
            String format = String.format("ws://%s:%d/group?client=%s", host, websocket.getPort(), clientPrefix + i);
            defaultWebSocketClientComponent.createNewClientAndConnect(new WebSocketClientConnectProperties(format));
        }

        TimeUnit.SECONDS.sleep(1);

        DefaultWebSocketClientProtocol.writer(clientConnectProperties, "触发写文本的组测试");
        DefaultWebSocketClientProtocol.writer(clientConnectProperties, "触发写二进制的组测试".getBytes(StandardCharsets.UTF_8));

        TimeUnit.SECONDS.sleep(2);
        DefaultWebSocketClientProtocol.close(clientConnectProperties, WebSocketCloseStatus.NORMAL_CLOSURE, "触发关闭客户端的组测试");
        TimeUnit.SECONDS.sleep(2);
    }

    @Override
    public int getOrder() {
        return 1500;
    }
}
