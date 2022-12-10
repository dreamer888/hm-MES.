package com.dream.iot.test.client.mutual;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.utils.ByteUtil;

import java.nio.charset.StandardCharsets;

public class MutualClientTestProtocol extends ServerInitiativeProtocol<MutualClientMessage> {

    private String readMsg;

    private IotTestProperties.MutualConnectProperties properties;

    public MutualClientTestProtocol(MutualClientMessage requestMessage) {
        super(requestMessage);
        properties = (IotTestProperties.MutualConnectProperties) getIotClient().getConfig();
    }

    @Override
    protected void doBuildRequestMessage(MutualClientMessage requestMessage) {
        byte[] message = requestMessage.getMessage();
        MutualType type = this.properties.getType();
        switch (type) {
            case HEX: readMsg = ByteUtil.bytesToHex(message); break;
            case UTF8: readMsg = new String(message, StandardCharsets.UTF_8); break;
            case ASCII: readMsg = new String(message, StandardCharsets.US_ASCII); break;
        }

        System.out.println("------------ 接收到服务端发送过来的["+type+"]报文：" + readMsg + " -------------------");
    }

    @Override
    protected MutualClientMessage doBuildResponseMessage() { return null; }

    @Override
    public ProtocolType protocolType() {
        return null;
    }

    public String getReadMsg() {
        return readMsg;
    }
}
