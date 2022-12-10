package com.dream.iot.server.component;

import com.dream.iot.*;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.TcpServerComponent;
import io.netty.channel.ChannelPipeline;

/**
 * create time: 2021/2/20
 *
 * @author dream
 * @since 1.0
 */
public abstract class TcpDecoderServerComponent<M extends ServerMessage> extends TcpServerComponent<M> implements IotProtocolFactory<M> {

    private ProtocolFactoryDelegation delegation;

    public TcpDecoderServerComponent(ConnectProperties connectProperties) {
        super(connectProperties);
        this.delegation = new ProtocolFactoryDelegation(this, protocolTimeoutStorage());
    }

    @Override
    public abstract String getName();

    @Override
    protected IotProtocolFactory createProtocolFactory() {
        return this;
    }

    @Override
    public void init(Object... args) {
        this.doInitChannel((ChannelPipeline) args[0]);
    }

    @Override
    public AbstractProtocol get(String key) {
        return this.delegation.get(key);
    }

    @Override
    public AbstractProtocol add(String key, Protocol val) {
        return this.delegation.add(key, val);
    }

    @Override
    public AbstractProtocol add(String key, Protocol protocol, long timeout) {
        return this.delegation.add(key, protocol, timeout);
    }

    @Override
    public AbstractProtocol remove(String key) {
        return this.delegation.remove(key);
    }

    @Override
    public boolean isExists(String key) {
        return this.delegation.isExists(key);
    }

    @Override
    public Object getStorage() {
        return this.delegation.getStorage();
    }

}
