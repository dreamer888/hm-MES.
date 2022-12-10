package com.dream.iot.test.client.breaker;

import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.message.DefaultMessageBody;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.test.BreakerProtocolType;
import com.dream.iot.test.MessageCreator;
import com.dream.iot.test.StatusCode;
import com.dream.iot.test.TestConst;
import com.dream.iot.utils.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据推送协议
 */
public class DataPushProtocol extends ClientInitiativeProtocol<BreakerClientMessage> {

    private String deviceSn;

    private static Logger logger = LoggerFactory.getLogger(DataPushProtocol.class);

    public DataPushProtocol(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override
    protected BreakerClientMessage doBuildRequestMessage() {
        DefaultMessageBody messageBody = MessageCreator.buildBreakerBody();

        DefaultMessageHead messageHead = MessageCreator.buildBreakerHeader(Long
                .valueOf(this.deviceSn), messageBody.getLength(), this.protocolType());

        return new BreakerClientMessage(messageHead, messageBody);
    }

    @Override
    public void doBuildResponseMessage(BreakerClientMessage responseMessage) {
        byte[] message = responseMessage.getMessage();
        int status = ByteUtil.bytesToInt(message, message.length - 4);
        StatusCode code = StatusCode.getInstance(status);
        if(code != StatusCode.Success) {
            ClientComponent component = IotClientBootstrap.getClientComponent(BreakerClientMessage.class);
            logger.error(TestConst.LOGGER_PROTOCOL_DESC, component.getName(), protocolType().desc, getEquipCode(), getMessageId(), "不通过("+code.desc+")");
        }
    }

    @Override
    public BreakerProtocolType protocolType() {
        return BreakerProtocolType.PushData;
    }
}
