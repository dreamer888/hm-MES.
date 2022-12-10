package com.dream.iot.proxy;

import com.alibaba.fastjson.annotation.JSONType;
import com.dream.iot.Message;
import com.dream.iot.utils.UniqueIdGen;

import java.beans.Transient;

@JSONType(ignores = {"message", "length"})
public class ProxyClientMessageHead implements Message.MessageHead {

    private byte[] message;

    /**
     * 调用客户端的超时时间(ms)
     * 0 使用设备协议默认时间
     * -1 不等待直接返回
     */
    private long timeout;

    /**
     * 客户端编号
     */
    private String equipCode;

    private String messageId;

    private ProxyClientType type;

    public ProxyClientMessageHead() {
        this(0);
    }

    public ProxyClientMessageHead(long timeout) {
        this(ProxyClientType.Proxy_Client_Server, timeout);
    }

    public ProxyClientMessageHead(ProxyClientType type, long timeout) {
        this.type = type;
        this.timeout = timeout;
        this.messageId = UniqueIdGen.messageId();
    }

    @Override
    public String getEquipCode() {
        return this.equipCode;
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public ProxyClientType getType() {
        return this.type;
    }


    @Override
    @Transient
    public byte[] getMessage() {
        return this.message;
    }

    @Override
    @Transient
    public int getLength() {
        return this.getMessage().length;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setType(ProxyClientType type) {
        this.type = type;
    }

    public long getTimeout() {
        return timeout;
    }

    public ProxyClientMessageHead setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public boolean isWaiting() {
        return timeout != -1;
    }

    @Override
    public String toString() {
        return "ProxyClientMessageHead{" +
                "timeout=" + timeout +
                ", equipCode='" + equipCode + '\'' +
                ", messageId='" + messageId + '\'' +
                ", type=" + type +
                '}';
    }
}
