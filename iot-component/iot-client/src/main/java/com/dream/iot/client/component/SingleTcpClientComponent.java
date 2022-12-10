package com.dream.iot.client.component;

import com.dream.iot.client.*;
import io.netty.channel.ChannelInboundHandler;

import java.util.List;

/**
 * 此适配器可以自定义实现各个组件的实现细节
 * 只能使用单客户端
 * @see MultiClientManager 无效
 * @param <M>
 */
public abstract class SingleTcpClientComponent<M extends ClientMessage> extends TcpClientComponent<M> {

    private TcpSocketClient tcpSocketClient;
    private static final MultiClientManager clientManager = new SingleClientManager();

    public SingleTcpClientComponent(ClientConnectProperties config) {
        super(config, clientManager);
        this.setClientComponent(this);
        this.tcpSocketClient = new TcpSocketClient(this, config) {
            @Override
            protected ChannelInboundHandler createProtocolDecoder() {
                return SingleTcpClientComponent.this.createProtocolDecoder();
            }
        };
    }

    @Override
    public TcpSocketClient getClient(Object clientKey) {
        return tcpSocketClient;
    }

    protected abstract ChannelInboundHandler createProtocolDecoder();

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    @Override
    public String getDesc() {
        return "Tcp客户端组件("+this.getClass().getSimpleName()+")";
    }


    @Override
    public TcpSocketClient getClient() {
        return this.tcpSocketClient;
    }

    protected static class SingleClientManager implements MultiClientManager {

        @Override
        public void addClient(Object clientKey, IotClient value) {

        }

        @Override
        public IotClient getClient(Object clientKey) {
            return null;
        }

        @Override
        public IotClient removeClient(Object clientKey) {
            throw new UnsupportedOperationException("不支持此操作");
        }

        @Override
        public List<IotClient> clients() {
            throw new UnsupportedOperationException("不支持此操作");
        }

        @Override
        public ClientComponent getClientComponent() {
            return null;
        }

        @Override
        public void setClientComponent(ClientComponent component) {

        }
    }
}
