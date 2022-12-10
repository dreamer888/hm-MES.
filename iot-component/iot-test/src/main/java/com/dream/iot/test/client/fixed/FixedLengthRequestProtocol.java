package com.dream.iot.test.client.fixed;

import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.utils.ByteUtil;

public class FixedLengthRequestProtocol extends ClientInitiativeProtocol<FixedLengthClientMessage> {

    /**
     * 当前服务器连接数量
     */
    private long clientNum;

    private FixedLengthClientMessage requestMessage;

    public FixedLengthRequestProtocol(FixedLengthClientMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    protected FixedLengthClientMessage doBuildRequestMessage() {
        return this.requestMessage;
    }

    @Override
    public void doBuildResponseMessage(FixedLengthClientMessage responseMessage) {
        if(getExecStatus() == ExecStatus.success) {
            byte[] message = responseMessage.getMessage();
            this.clientNum = ByteUtil.bytesToLong(message, 20);
        }
    }

    @Override
    public TestProtocolType protocolType() {
        return TestProtocolType.CIReq;
    }

    public long getClientNum() {
        return clientNum;
    }

    public void setClientNum(long clientNum) {
        this.clientNum = clientNum;
    }
}
