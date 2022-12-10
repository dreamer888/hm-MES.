package com.dream.iot.test.message.line;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.dream.iot.ProtocolType;
import com.dream.iot.test.SystemInfo;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.TMessageHead;

@JSONType(ignores = {"message"})
public class LineMessageHead extends TMessageHead {

    private SystemInfo payload;

    protected LineMessageHead() {
        super(null, null, null);
    }

    protected LineMessageHead(byte[] message) {
        super(message);
    }

    protected LineMessageHead(String equipCode, String messageId, ProtocolType type) {
        super(equipCode, messageId, type);
    }

    public static LineMessageHead buildHeader(byte[] message) {
        JSONObject object = JSONObject.parseObject(new String(message));
        LineMessageHead messageHead = object.toJavaObject(LineMessageHead.class);
        messageHead.setType(object.getObject("type", TestProtocolType.class));
        return messageHead;
    }

    public static LineMessageHead buildHeader(String equipCode, String messageId, ProtocolType type, SystemInfo payload) {
        LineMessageHead lineMessageHead = new LineMessageHead(equipCode, messageId, type);
        lineMessageHead.setPayload(payload);
        byte[] bytes = JSONObject.toJSONBytes(lineMessageHead);

        lineMessageHead.setMessage(bytes);
        return lineMessageHead;
    }

    @Override
    @JSONField(deserialize = false)
    public void setType(ProtocolType type) {
        super.setType(type);
    }

    public SystemInfo getPayload() {
        return payload;
    }

    public void setPayload(SystemInfo payload) {
        this.payload = payload;
    }
}
