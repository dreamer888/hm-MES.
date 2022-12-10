package com.dream.iot.server.endpoints;

import com.alibaba.fastjson.JSON;
import com.dream.iot.server.ServerComponentFactory;
import com.dream.iot.server.websocket.WebSocketServerListener;
import com.dream.iot.server.websocket.impl.DefaultWebSocketServerProtocol;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 应用服务详情信息
 */
public class ServerHealthWebsocketEndpoint implements WebSocketServerListener, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final ServerComponentFactory componentFactory;

    public ServerHealthWebsocketEndpoint(ServerComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    @Override
    public String uri() {
        return "/ws/endpoint/health";
    }

    @Override
    public void onText(DefaultWebSocketServerProtocol protocol) {
        String requestText = protocol.readText();
        Optional<String> type = protocol.requestMessage().getQueryParam("type");
        if(StringUtils.hasText(requestText) && !type.isPresent()) {
            try {
                type = Optional.ofNullable(JSON.parseObject(requestText).getString("type"));
            } catch (Exception e) {
                protocol.response(Result.fail("请求参数只支持Json格式").textJson());
                return;
            }
        }

        if(type.isPresent()) {
            Result info;
            switch (type.get()) {
                case "jvm":
                    info = Result.success(ServerHealthBuilder.buildJvmInfo());
                    break;
                case "system":
                    info = Result.success(ServerHealthBuilder.buildSystemInfo());
                    break;
                case "server":
                    info = Result.success(ServerHealthBuilder.buildComponentInfo(componentFactory));
                    break;
                default: info = Result.fail("错误的参数值["+type+"], 可选值[jvm, system, server]");
            }

            protocol.response(info.textJson());
        } else {
            long startupDate = this.applicationContext.getStartupDate();
            Result result = ServerHealthBuilder.toResult(startupDate, this.componentFactory);
            protocol.response(result.textJson());
        }
    }

    @Override
    public void onClose(DefaultWebSocketServerProtocol protocol) {

    }

    @Override
    public void onBinary(DefaultWebSocketServerProtocol protocol) {
        protocol.response(Result.fail("不支持使用二进制请求").binaryJson());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
