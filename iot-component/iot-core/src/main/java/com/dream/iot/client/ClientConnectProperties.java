

package com.dream.iot.client;

import com.dream.iot.config.ConnectProperties;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 客户端连接配置
 */
public class ClientConnectProperties extends ConnectProperties {

    /**
     * 断开重连时间周期(秒)
     * 重试5次, 每次在次基础上递增
     */
    private long reconnect = 15;

    /**
     * 客户端唯一标识key
     * @see MultiClientManager#getClient(Object)
     */
    private String connectKey;

    /**
     * 本地主机地址
     * @see #getHost() 远程服务器主机
     */
    private String localHost;

    /**
     * 本地主机端口
     * @see #getPort() 远程服务器端口
     */
    private Integer localPort;

    public ClientConnectProperties() { }

    public ClientConnectProperties(Integer remotePort) {
        this("127.0.0.1", remotePort);
    }

    public ClientConnectProperties(String remoteHost, Integer remotePort) {
        super(remoteHost, remotePort);
    }

    public ClientConnectProperties(String remoteHost, Integer remotePort, Integer localPort) {
        super(remoteHost, remotePort);
        this.localPort = localPort;
    }

    public ClientConnectProperties(String remoteHost, Integer remotePort, String connectKey) {
        super(remoteHost, remotePort);
        this.connectKey = connectKey;
    }

    public ClientConnectProperties(String remoteHost, Integer remotePort, String localHost, Integer localPort) {
        super(remoteHost, remotePort);
        this.localHost = localHost;
        this.localPort = localPort;
    }

    public ClientConnectProperties(Integer remotePort, long allIdleTime, long readerIdleTime, long writerIdleTime) {
        this("127.0.0.1", remotePort, allIdleTime, readerIdleTime, writerIdleTime);
    }

    public ClientConnectProperties(String remoteHost, Integer remotePort, long allIdleTime, long readerIdleTime, long writerIdleTime) {
        super(remoteHost, remotePort, allIdleTime, readerIdleTime, writerIdleTime);
    }

    public SocketAddress remoteSocketAddress() {
        return new InetSocketAddress(this.getHost(), this.getPort());
    }

    public SocketAddress localSocketAddress() {
        if(this.getLocalHost() == null && this.getLocalPort() == null) {
            return null;
        } else if(StringUtils.hasText(this.getLocalHost()) && this.getLocalPort() != null) {
            return new InetSocketAddress(this.getLocalHost(), this.getLocalPort());
        } else if(this.getLocalPort() != null) {
            return new InetSocketAddress(this.getLocalPort());
        } else if(this.getLocalHost() != null){
            return new InetSocketAddress(this.getLocalHost(), 0);
        } else {
            return null;
        }
    }

    /**
     * 使用主机和端口号来标识客户端的唯一性
     * @see MultiClientManager
     * @return
     */
    public String connectKey() {
        if(connectKey == null) {
            this.connectKey = getHost() + ":" + getPort();
        }

        return connectKey;
    }

    public ClientConnectProperties setConnectKey(String connectKey) {
        this.connectKey = connectKey;
        return this;
    }

    public long getReconnect() {
        return reconnect;
    }

    public void setReconnect(long reconnect) {
        this.reconnect = reconnect;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    @Override
    public String toString() {
        return this.connectKey();
    }
}
