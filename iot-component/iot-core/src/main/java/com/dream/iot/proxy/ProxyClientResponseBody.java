package com.dream.iot.proxy;

import com.alibaba.fastjson.annotation.JSONType;
import com.dream.iot.consts.ExecStatus;

/**
 * 应用程序客户端响应报文体
 * @see ProxyClientMessage#getBody()
 */
@JSONType(ignores = {"message", "length"})
public class ProxyClientResponseBody implements ProxyClientMessageBody {

    // 响应的数据
    private Object data;
    // 问题描述
    private String reason;
    // 响应状态
    private ExecStatus status;

    private byte[] message;

    public ProxyClientResponseBody(String reason, ExecStatus status) {
        this.reason = reason;
        this.status = status;
    }

    public ProxyClientResponseBody(Object data, String reason, ExecStatus status) {
        this.data = data;
        this.reason = reason;
        this.status = status;
    }

    public static ProxyClientResponseBody success(String reason) {
        return new ProxyClientResponseBody(reason, ExecStatus.success);
    }

    public static ProxyClientResponseBody success() {
        return success("OK");
    }

    public ExecStatus getStatus() {
        return status;
    }

    public ProxyClientResponseBody setStatus(ExecStatus status) {
        this.status = status;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public ProxyClientResponseBody setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ProxyClientResponseBody setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String getDeviceSn() {
        return null;
    }

    @Override
    public String getCtrl() {
        return null;
    }

    @Override
    public byte[] getMessage() {
        return message;
    }

    @Override
    public ProxyClientMessageBody setMessage(byte[] message) {
        this.message = message;
        return this;
    }
}
