package com.dream.iot.client.component;

import com.dream.iot.client.*;
import com.dream.iot.SocketMessage;
import com.dream.iot.AbstractProtocol;
import com.dream.iot.IotProtocolFactory;

/**
 * 默认的客户端
 */
public class DefaultClientComponent extends SocketClientComponent {

    private String name;
    private String desc;
    private SocketClient socketClient;
    private ClientFactory clientFactory;
    private IotProtocolFactory protocolFactory;
    private Class<? extends ClientMessage> messageClass;

    /**
     * 单客户端 一个组件对应一个客户端
     * @param socketClient
     * @param protocolFactory
     * @param messageClass
     */
    public DefaultClientComponent(SocketClient socketClient
            , IotProtocolFactory protocolFactory, Class<? extends ClientMessage> messageClass) {
        this(messageClass.getSimpleName(), messageClass.getSimpleName(), socketClient, protocolFactory, messageClass);
    }

    /**
     * 单客户端 一个组件对应一个客户端
     * @param name
     * @param desc
     * @param socketClient
     * @param protocolFactory
     * @param messageClass
     */
    public DefaultClientComponent(String name, String desc, SocketClient socketClient
            , IotProtocolFactory protocolFactory, Class<? extends ClientMessage> messageClass) {
        super(socketClient.getConfig());
        this.name = name;
        this.desc = desc;
        this.socketClient = socketClient;
        this.messageClass = messageClass;
        this.protocolFactory = protocolFactory;
        if(this.socketClient.getClientComponent() == null) {
            this.socketClient.setClientComponent(this);
        }
    }

    /**
     * 多客户端  一个组件对应多个客户端(用于一套协议对应多台服务器 如分布式服务集群)
     * @param name
     * @param desc
     * @param config
     * @param clientFactory 创建客户端
     * @param protocolFactory
     * @param messageClass
     */
    public DefaultClientComponent(String name, String desc, ClientConnectProperties config
            , ClientFactory clientFactory, IotProtocolFactory protocolFactory
            , Class<? extends ClientMessage> messageClass) {
        super(config);
        this.name = name;
        this.desc = desc;
        this.clientFactory = clientFactory;
        this.messageClass = messageClass;
        this.protocolFactory = protocolFactory;
    }

    @Override
    public SocketClient createNewClient(ClientConnectProperties config) {
        if(this.clientFactory != null) {
            return (SocketClient) this.clientFactory.createNewClient(config);
        }

        return this.socketClient;
    }

    @Override
    public IotProtocolFactory protocolFactory() {
        return this.protocolFactory;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public Class<? extends ClientMessage> getMessageClass() {
        return this.messageClass;
    }

    public SocketClientComponent setName(String name) {
        this.name = name;
        return this;
    }

    public SocketClientComponent setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    @Override
    public AbstractProtocol getProtocol(SocketMessage message) {
        throw new UnsupportedOperationException("不支持此方法");
    }

}
