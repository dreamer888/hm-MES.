package com.dream.iot.server.websocket;

import com.dream.iot.websocket.WebSocketFrameType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来声明websocket处理类
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IotWebsocket {

    /**
     * 要处理的uri
     * @return
     */
    String uri();

    /**
     * 过滤出要处理的类型
     * @return
     */
    WebSocketFrameType[] filter() default {WebSocketFrameType.Text, WebSocketFrameType.Binary, WebSocketFrameType.Close};
}
