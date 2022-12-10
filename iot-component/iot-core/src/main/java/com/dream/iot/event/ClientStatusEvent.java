package com.dream.iot.event;

import com.dream.iot.FrameworkComponent;

/**
 * 客户端状态事件
 */
public class ClientStatusEvent extends IotFrameworkEvent {

    private ClientStatus status;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ClientStatusEvent(Object source, ClientStatus status) {
        super(source);
        this.status = status;
    }

    public ClientStatusEvent(Object source, ClientStatus status, FrameworkComponent component) {
        this(source, status);
        this.setComponent(component);
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }
}
