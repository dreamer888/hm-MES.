package com.dream.iot.client.udp;

import com.dream.iot.client.ClientConnectProperties;

import java.net.InetSocketAddress;

/**
 * Udp协议客户端连接配置
 */
public class UdpClientConnectProperties extends ClientConnectProperties {

    private InetSocketAddress sender;
    private InetSocketAddress recipient;

    public UdpClientConnectProperties() { }

    /**
     * @param recipientHost 远程接收方主机地址
     * @param recipientPort 远程接收方主机端口
     */
    public UdpClientConnectProperties(String recipientHost, Integer recipientPort) {
        super(recipientHost, recipientPort);
        this.recipient = InetSocketAddress.createUnresolved(recipientHost, recipientPort);
    }

    /**
     * @param recipientHost 远程接收方主机地址
     * @param recipientPort 远程接收方主机端口
     * @param senderHost 本地发送方主机地址
     * @param senderPort 本地发送方主机端口
     */
    public UdpClientConnectProperties(String recipientHost, Integer recipientPort, String senderHost, Integer senderPort) {
        super(recipientHost, recipientPort, senderHost, senderPort);
        this.sender = InetSocketAddress.createUnresolved(senderHost, senderPort);
        this.recipient = InetSocketAddress.createUnresolved(recipientHost, recipientPort);
    }

    public InetSocketAddress getSender() {
        if(this.sender == null) {
            this.sender = InetSocketAddress.createUnresolved(this.getLocalHost(), this.getLocalPort());
        }

        return sender;
    }

    public void setSender(InetSocketAddress sender) {
        this.sender = sender;
    }

    public InetSocketAddress getRecipient() {
        if(this.recipient == null) {
            this.recipient = InetSocketAddress.createUnresolved(this.getHost(), this.getPort());
        }

        return recipient;
    }

    public void setRecipient(InetSocketAddress recipient) {
        this.recipient = recipient;
    }
}
