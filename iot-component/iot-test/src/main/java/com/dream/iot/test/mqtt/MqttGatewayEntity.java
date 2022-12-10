package com.dream.iot.test.mqtt;

import java.util.Date;

public class MqttGatewayEntity {

    private double i = 3;

    private Date createTime;

    public MqttGatewayEntity() {
        this.createTime = new Date();
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
