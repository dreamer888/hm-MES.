package com.dream.iot.plc.omron;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;

/**
 * 欧姆龙PLC编解码组件
 */
public class OmronComponent extends TcpClientComponent<OmronMessage> {

    public OmronComponent() { }

    public OmronComponent(OmronConnectProperties config) {
        super(config);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new OmronTcpClient(this, (OmronConnectProperties) config);
    }

    @Override
    public String getName() {
        return "欧姆龙PLC";
    }

    @Override
    public String getDesc() {
        return getName();
    }

    @Override
    public Class<OmronMessage> getMessageClass() {
        return OmronMessage.class;
    }

    @Override
    public AbstractProtocol getProtocol(OmronMessage message) {
        return remove(message.getMessageId());
    }
}
