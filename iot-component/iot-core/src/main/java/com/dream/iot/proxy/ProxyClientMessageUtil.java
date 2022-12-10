package com.dream.iot.proxy;

import com.alibaba.fastjson.JSONObject;
import com.dream.iot.utils.ByteUtil;

public class ProxyClientMessageUtil {


    public static ProxyClientMessage encoder(ProxyClientMessage message) {
        doEncoder(message.getHead(), message.getBody());
        return message;
    }

    public static ProxyServerMessage encoder(ProxyServerMessage message) {
        doEncoder(message.getHead(), message.getBody());
        return message;
    }

    /**
     * 报文格式
     * +--------------------------------------------------------
     * | totalLengthField + headLengthField + headMsg + bodyMsg
     * +--------------------------------------------------------
     * @return
     */
    private static void doEncoder(ProxyClientMessageHead head, ProxyClientMessageBody body) {
        byte[] headMsg = JSONObject.toJSONBytes(head);
        byte[] bodyMsg = JSONObject.toJSONBytes(body);

        // 报文头 + totalLengthField + headLengthField
        int headLength = headMsg.length + 4 + 4;
        byte[] headTotalMessage = new byte[headLength];

        // 总长度字段 = 头长度字段(4) + 报文头长度 + 报文体长度
        int totalLength = headLength + bodyMsg.length - 4;
        ByteUtil.addBytes(headTotalMessage, ByteUtil.getBytes(totalLength), 0);
        ByteUtil.addBytes(headTotalMessage, ByteUtil.getBytes(headMsg.length), 4);
        ByteUtil.addBytes(headTotalMessage, headMsg, 8);

        head.setMessage(headTotalMessage);
        body.setMessage(bodyMsg);
    }

    public static ProxyClientMessageHead decoder(byte[] message) {
        int headLength = ByteUtil.bytesToInt(message, 0);
        byte[] headMsg = ByteUtil.subBytes(message, 4, headLength + 4);

        return JSONObject.parseObject(headMsg, ProxyClientMessageHead.class);
    }
}
