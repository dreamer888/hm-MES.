package com.dream.iot.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iot.client")
public class ClientProperties {

    /**
     * 应用程序客户端代理配置
     */
    private ClientProxyConnectProperties proxy;

    public static class ClientProxyConnectProperties extends ClientConnectProperties {

        /**
         * 是否启用代理客户端
         */
        private boolean start;

        /**
         * 心跳周期(秒)
         */
        private int heart = 60;

        /**
         * 代理客户端编号(默认随机生成)
         */
        private String deviceSn;

        /**
         * 报文最大长度
         */
        private int maxFrameLength = 1024 * 1024;

        @Override
        public String connectKey() {
            return this.deviceSn;
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }

        public int getHeart() {
            return heart;
        }

        public void setHeart(int heart) {
            this.heart = heart;
        }

        public String getDeviceSn() {
            return deviceSn;
        }

        public void setDeviceSn(String deviceSn) {
            this.deviceSn = deviceSn;
        }

        public int getMaxFrameLength() {
            return maxFrameLength;
        }

        public void setMaxFrameLength(int maxFrameLength) {
            this.maxFrameLength = maxFrameLength;
        }
    }

    public ClientProxyConnectProperties getProxy() {
        return proxy;
    }

    public void setProxy(ClientProxyConnectProperties proxy) {
        this.proxy = proxy;
    }

}
