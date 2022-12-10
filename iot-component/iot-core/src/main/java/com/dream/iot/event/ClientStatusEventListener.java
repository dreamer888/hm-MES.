package com.dream.iot.event;

import com.dream.iot.FrameworkComponent;
import org.springframework.context.ApplicationListener;

/**
 * 客户端状态事件监听
 * @see ClientStatusEvent
 * @see ClientStatus
 */
public interface ClientStatusEventListener extends ApplicationListener<ClientStatusEvent> {

    /**
     * 是否交由此监听器处理
     * 用于处理多个存在多个监听器的情况
     * @param event
     * @return
     */
    default boolean matcher(ClientStatusEvent event) {
        return true;
    }

    @Override
    default void onApplicationEvent(ClientStatusEvent event) {
        if(event == null) {
            throw new IllegalArgumentException("未指定设备事件参数");
        }

        if(this.matcher(event)) {
            switch (event.getStatus()) {
                case online:
                    online(event.getSource(), event.getComponent());
                    break;
                case offline:
                    offline(event.getSource(), event.getComponent());
                    break;

                default: throw new IllegalStateException("错误的设备事件类型: " + event.getStatus());
            }
        }
    }

    /**
     * 注册成功处理
     * @param source
     */
    void online(Object source, FrameworkComponent component);

    /**
     * 连接断开处理
     * @param source
     */
    void offline(Object source, FrameworkComponent component);
}
