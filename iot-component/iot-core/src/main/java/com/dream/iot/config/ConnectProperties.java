package com.dream.iot.config;

import com.dream.iot.ssl.SslConfig;

public class ConnectProperties {

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 没读和没写多长时间判断失活(秒)
     */
    private long allIdleTime;

    /**
     * 多久没读判断失活的时间(秒)
     */
    private long readerIdleTime;

    /**
     * 多久没写判断失活的时间(秒)
     */
    private long writerIdleTime;

    /**
     * ssl证书路径
     */
    private SslConfig ssl;

    public ConnectProperties() {
        this(0);
    }

    /**
     * host默认为null 说明不绑定任何网卡
     * @param port
     */
    public ConnectProperties(Integer port) {
        this(null, port);
    }

    public ConnectProperties(String host, Integer port) {
        this(host, port, 0, 0, 0);
    }

    /**
     * host默认为null 说明不绑定任何网卡
     * @param port
     */
    public ConnectProperties(Integer port, long allIdleTime, long readerIdleTime, long writerIdleTime) {
        this(null, port, allIdleTime, readerIdleTime, writerIdleTime);
    }

    public ConnectProperties(String host, Integer port, long allIdleTime, long readerIdleTime, long writerIdleTime) {
        this.host = host;
        this.port = port;
        this.allIdleTime = allIdleTime;
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getAllIdleTime() {
        return allIdleTime;
    }

    public void setAllIdleTime(long allIdleTime) {
        this.allIdleTime = allIdleTime;
    }

    public long getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(long readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public long getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(long writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }

    public SslConfig getSsl() {
        return ssl;
    }

    public void setSsl(SslConfig ssl) {
        this.ssl = ssl;
    }

    @Override
    public String toString() {
        return this.host + ":" + this.port;
    }
}
