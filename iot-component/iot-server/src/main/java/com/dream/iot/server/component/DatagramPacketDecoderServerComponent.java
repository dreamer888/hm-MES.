package com.dream.iot.server.component;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.IotProtocolFactory;
import com.dream.iot.Protocol;
import com.dream.iot.ProtocolFactoryDelegation;
import com.dream.iot.codec.adapter.DatagramPacketDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.IotSocketServer;
import com.dream.iot.server.udp.UdpServerComponent;
import com.dream.iot.server.udp.UdpServerMessage;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

/**
 * 适配Udp DatagramPacket解码组件
 * @param <M>
 */
public abstract class DatagramPacketDecoderServerComponent<M extends UdpServerMessage>
        extends UdpServerComponent<M> implements IotSocketServer, IotProtocolFactory<M> {

    private ProtocolFactoryDelegation delegation;
    private DatagramPacketDecoderAdapter datagramPacketSimpleHandle;

    public DatagramPacketDecoderServerComponent(int port) {
        this(new ConnectProperties(port));
    }

    public DatagramPacketDecoderServerComponent(ConnectProperties config) {
        super(config);
        this.datagramPacketSimpleHandle = new DatagramPacketDecoderAdapter();
        this.delegation = new ProtocolFactoryDelegation(this, protocolTimeoutStorage());
    }

    @Override
    public abstract String getName();

    @Override
    public AbstractProtocol get(String key) {
        return this.delegation.get(key);
    }

    @Override
    public AbstractProtocol add(String key, Protocol val) {
        return this.delegation.add(key, val);
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

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return this.datagramPacketSimpleHandle;
    }

    @Override
    protected IotSocketServer createDeviceServer() {
        return this;
    }

    @Override
    protected IotProtocolFactory createProtocolFactory() {
        return this.delegation;
    }

    @Override
    public Protocol add(String key, Protocol protocol, long timeout) {
        return this.delegation.add(key, protocol, timeout);
    }

    @Override
    public void init(Object... args) {
        this.doInitChannel((ChannelPipeline) args[0]);
    }

}
