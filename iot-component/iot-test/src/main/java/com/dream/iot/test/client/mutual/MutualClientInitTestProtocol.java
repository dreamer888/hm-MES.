package com.dream.iot.test.client.mutual;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.utils.ByteUtil;

import java.nio.charset.StandardCharsets;

public class MutualClientInitTestProtocol extends ClientInitiativeProtocol<MutualClientMessage> {

    private String text;
    private String deviceSn;
    private MutualType type;

    public MutualClientInitTestProtocol(String deviceSn, String text) {
        this.text = text;
        this.deviceSn = deviceSn;
    }

    @Override
    protected MutualClientMessage doBuildRequestMessage() {
        IotTestProperties.MutualConnectProperties config = (IotTestProperties.MutualConnectProperties) getIotClient().getConfig();
        this.type = config.getType();
        DefaultMessageHead head = new DefaultMessageHead(this.deviceSn, null, null);
        if(type == MutualType.HEX) {
            head.setMessage(ByteUtil.hexToByte(text));
        } else if(type == MutualType.UTF8) {
            head.setMessage(text.getBytes(StandardCharsets.UTF_8));
        } else {
            head.setMessage(text.getBytes(StandardCharsets.US_ASCII));
        }

        return new MutualClientMessage(head);
    }

    @Override
    public void doBuildResponseMessage(MutualClientMessage responseMessage) { }

    @Override
    public ProtocolType protocolType() {
        return null;
    }

}
