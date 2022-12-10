package com.dream.iot.proxy;

import com.dream.iot.Message;

public interface ProxyClientMessageBody extends Message.MessageBody {

    static ProxyClientJsonMessageBody jsonMessageBody() {
        return new ProxyClientJsonMessageBody();
    }

    static ProxyClientJsonMessageBody jsonMessageBody(String key, Object value) {
        return new ProxyClientJsonMessageBody().add(key, value);
    }

    /**
     * 代理设备编号
     * @return
     */
    String getDeviceSn();

    /**
     * ctrl字符串 {@code com.dream.network.client.handle.IotCtrl#value()}
     * @return
     */
    String getCtrl();

    ProxyClientMessageBody setMessage(byte[] message);
}
