package com.dream.iot.client.proxy;

import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.protocol.CommonProtocolType;
import com.dream.iot.proxy.ProxyClientMessage;
import com.dream.iot.proxy.ProxyClientMessageUtil;
import com.dream.iot.proxy.ProxyClientResponseBody;

/**
 * 应用程序客户端和服务端通信的协议
 */
public class ProxyClientProtocol extends ClientInitiativeProtocol<ProxyClientMessage> {

    /**
     * 执行状态说明
     */
    private String reason;

    public ProxyClientProtocol(ProxyClientMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    protected ProxyClientMessage doBuildRequestMessage() {
        // 设置客户端编号
        ProxyClient iotClient = (ProxyClient) getIotClient();
        this.requestMessage.getHead().setEquipCode(iotClient.getDeviceSn());

        return ProxyClientMessageUtil.encoder(this.requestMessage);
    }

    public static ProxyClientProtocol buildRequestProtocol(ProxyClientMessage message) {
        return new ProxyClientProtocol(message);
    }

    @Override
    public void doBuildResponseMessage(ProxyClientMessage message) {
        ProxyClientResponseBody body = (ProxyClientResponseBody)message.getBody();

        this.reason = body.getReason();
        this.setExecStatus(body.getStatus());
    }

    @Override
    public ProxyClientProtocol timeout(long timeout) {
        return (ProxyClientProtocol) super.timeout(timeout);
    }

    @Override
    public CommonProtocolType protocolType() {
        return CommonProtocolType.TCClint;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
