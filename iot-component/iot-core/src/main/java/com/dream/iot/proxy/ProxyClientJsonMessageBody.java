package com.dream.iot.proxy;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.annotation.JSONType;

@JSONType(ignores = {"message", "length"})
public class ProxyClientJsonMessageBody implements ProxyClientMessageBody {

    private String ctrl;
    private byte[] message;
    private String deviceSn;
    private JSONObject data = new JSONObject();

    public ProxyClientJsonMessageBody() { }

    /**
     * @param deviceSn 需要代理的设备编号
     * @param ctrl 控制字符串
     */
    public ProxyClientJsonMessageBody(String deviceSn, String ctrl) {
        this.deviceSn = deviceSn;
        this.ctrl = ctrl;
    }

    public ProxyClientJsonMessageBody add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    @Override
    public String getDeviceSn() {
        return this.deviceSn;
    }

    @Override
    public String getCtrl() {
        return this.ctrl;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public Object value(String key) {
        return this.data.get(key);
    }

    public Object resolvePath(String path) {
        return JSONPath.eval(this.data, path);
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public byte[] getMessage() {
        return message;
    }

    public ProxyClientJsonMessageBody setMessage(byte[] message) {
        this.message = message;
        return this;
    }
}
