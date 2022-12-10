package com.dream.iot.event;

import org.springframework.scheduling.annotation.Async;

/**
 * 异步事件
 * @see org.springframework.scheduling.annotation.EnableAsync 应用必须启用此注解
 */
public interface AsyncClientStatusEventListener extends ClientStatusEventListener {

    @Async
    @Override
    default void onApplicationEvent(ClientStatusEvent event) {
        ClientStatusEventListener.super.onApplicationEvent(event);
    }
}
