package com.dream.iot.plc.omron;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.UdpSocketClient;
import com.dream.iot.client.component.UdpClientComponent;
import com.dream.iot.client.udp.UdpClientConnectProperties;
import com.dream.iot.client.udp.UdpClientMessage;

/**
 * 欧姆龙PLC编解码组件
 */
public class OmronUdpComponent extends UdpClientComponent<OmronUdpClientMessage> {

    //public OmronUdpComponent() { }

    public OmronUdpComponent(OmronUdpConnectProperties config) {
        super(config);
    }

    @Override
    public UdpSocketClient createNewClient(ClientConnectProperties config) {
        return new OmronUdpClient( this, (UdpClientConnectProperties) config);
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
    public Class<OmronUdpClientMessage> getMessageClass() {
        return OmronUdpClientMessage.class;
    }

    @Override
    public AbstractProtocol getProtocol(OmronUdpClientMessage message) {
        return remove(message.getMessageId());
    }


}
