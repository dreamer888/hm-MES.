package com.dream.iot.test.server.breaker;

import com.dream.iot.Message;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.test.BreakerProtocolType;
import com.dream.iot.test.MessageCreator;
import com.dream.iot.test.StatusCode;
import com.dream.iot.utils.ByteUtil;

/**
 * @see BreakerProtocolType#PushData 用来接收断路器设备推送的电流电压数据协议
 */
public class DataAcceptProtocol extends ClientInitiativeProtocol<BreakerServerMessage> {

    private double v; // 电压
    private double i; // 电流
    private double power1; // 有功功率
    private double power2; // 无功功率
    private double py; // 功率因素

    public DataAcceptProtocol(BreakerServerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(BreakerServerMessage requestMessage) {
        byte[] message = requestMessage.getBody().getMessage();
        this.v = ByteUtil.bytesToInt(message, 0) / 100.0;
        this.i = ByteUtil.bytesToInt(message, 4) / 100.0;
        this.power1 = ByteUtil.bytesToInt(message, 8) / 100.0;
        this.power2 = ByteUtil.bytesToInt(message, 12) / 100.0;
        this.py = ByteUtil.bytesToShort(message, 16) / 100.0;
    }

    // 响应断路器的请求
    @Override
    protected BreakerServerMessage doBuildResponseMessage() {
        Message.MessageHead head = requestMessage().getHead();
        return new BreakerServerMessage(MessageCreator.buildBreakerHeader(head
                .getEquipCode(), head.getMessageId(), 4, head.getType()),
                MessageCreator.buildBreakerBody(StatusCode.Success));
    }

    @Override
    public BreakerProtocolType protocolType() {
        return BreakerProtocolType.PushData;
    }

    public double getV() {
        return v;
    }

    public double getI() {
        return i;
    }

    public double getPower1() {
        return power1;
    }

    public double getPower2() {
        return power2;
    }

    public double getPy() {
        return py;
    }
}
