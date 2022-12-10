package com.dream.iot.server.udp.impl;

import com.dream.iot.config.ConnectProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iot.server.udp.default")
public class DefaultUdpConnectProperties extends ConnectProperties {

    /**
     * 是否启用udp默认组件(7168端口)
     */
    private boolean start;

    /**
     * 默认使用168端口
     */
    public DefaultUdpConnectProperties() {
        super(7168);
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
