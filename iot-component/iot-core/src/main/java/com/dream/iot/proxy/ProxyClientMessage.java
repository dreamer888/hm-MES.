package com.dream.iot.proxy;

import com.alibaba.fastjson.JSONObject;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.utils.ByteUtil;

import static com.dream.iot.proxy.ProxyClientType.Proxy_Client_Heart;

/**
 * 应用程序客户端报文
 */
public class ProxyClientMessage<T extends ProxyClientMessageBody> extends ClientMessage {

    public ProxyClientMessage(byte[] message) {
        super(message);
    }

    public ProxyClientMessage(ProxyClientMessageHead head, ProxyClientMessageBody body) {
        super(head, body);
    }

    @Override
    protected ProxyClientMessageHead doBuild(byte[] message) {
        int headLength = ByteUtil.bytesToInt(message, 0);

        byte[] bodyMsg = ByteUtil.subBytes(message, headLength + 4);
        this.messageBody = JSONObject.parseObject(bodyMsg, ProxyClientResponseBody.class);

        return ProxyClientMessageUtil.decoder(message);
    }

    /**
     * 心跳报文
     * @return
     */
    public static ProxyClientMessage heart() {
        ProxyClientMessageHead head = new ProxyClientMessageHead(Proxy_Client_Heart, -1);
        return new ProxyClientMessage(head, new ProxyClientJsonMessageBody());
    }

    @Override
    public ProxyClientMessageHead getHead() {
        return (ProxyClientMessageHead) super.getHead();
    }

    @Override
    public T getBody() {
        return (T) super.getBody();
    }
}
