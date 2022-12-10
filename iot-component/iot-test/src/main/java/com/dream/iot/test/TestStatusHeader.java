package com.dream.iot.test;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.mqtt.message.MqttMessageHead;

import java.beans.Transient;

public class TestStatusHeader extends MqttMessageHead {

    // 操作的结果
    private boolean result;
    // 设备的开关状态
    private TestStatus status;

    public TestStatusHeader(byte[] message) {
        super(message);
    }

    public TestStatusHeader(String equipCode, String messageId, ProtocolType type, TestStatus status) {
        super(equipCode, messageId, type);
        this.status = status;
    }

    @Override
    @Transient
    public byte[] getMessage() {
        return super.getMessage();
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
