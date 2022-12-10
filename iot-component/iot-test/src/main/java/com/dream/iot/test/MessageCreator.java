package com.dream.iot.test;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.Message;
import com.dream.iot.message.DefaultMessageBody;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.test.client.breaker.BreakerClientMessage;
import com.dream.iot.test.client.fixed.FixedLengthClientMessage;
import com.dream.iot.test.server.fixed.FixedLengthServerMessage;
import com.dream.iot.utils.ByteUtil;

/**
 * 报文创造器
 */
public class MessageCreator {

    /**
     * 固定长度报文 28
     *    8         8        4       8
     * 设备编号 + messageId + type + 递增值
     */
    public static Message.MessageHead buildFixedMessageHead(byte[] message) {
        String deviceSn = ByteUtil.bytesToLong(message, 0) + "";
        String messageId = ByteUtil.bytesToLong(message, 8) + "";
        int type = ByteUtil.bytesToInt(message, 16);

        TestProtocolType protocolType = null;
        switch (type) {
            case 0: protocolType = TestProtocolType.PIReq; break;
            case 1: protocolType = TestProtocolType.CIReq; break;
            case 2: protocolType = TestProtocolType.Heart; break;
        }

        return new DefaultMessageHead(deviceSn, messageId, protocolType);
    }

    public static FixedLengthClientMessage buildFixedLengthClientMessage(long deviceSn, String messageId, TestProtocolType protocolType) {
        DefaultMessageHead messageHead = buildFixedMessageHeader(deviceSn, messageId, -1, protocolType);
        return new FixedLengthClientMessage(messageHead);
    }

    public static FixedLengthClientMessage buildFixedLengthClientMessage(long deviceSn, long incVal, TestProtocolType protocolType) {
        DefaultMessageHead messageHead = buildFixedMessageHeader(deviceSn, ClientSnGen.getMessageId(), incVal, protocolType);
        return new FixedLengthClientMessage(messageHead);
    }

    public static FixedLengthServerMessage buildFixedLengthServerMessage(String equipCode, String messageId, long clientNum, TestProtocolType tradeType) {
        DefaultMessageHead defaultMessageHead = buildFixedMessageHeader(Long.valueOf(equipCode), messageId, clientNum, tradeType);
        return new FixedLengthServerMessage(defaultMessageHead);
    }

    /**
     * 固定长度报文 28
     *    8         8        4       8
     * 设备编号 + messageId + type + 递增值
     */
    private static DefaultMessageHead buildFixedMessageHeader(long deviceSn, String messageId, long incVal, TestProtocolType protocolType) {
        DefaultMessageHead messageHead = new DefaultMessageHead(deviceSn + "", messageId, protocolType);

        int ordinal = protocolType.ordinal();
        byte[] type = ByteUtil.getBytes(ordinal); // 协议类型
        byte[] inc = ByteUtil.getBytes(incVal); // 递增值 心跳默认0
        byte[] bytes = ByteUtil.getBytes(deviceSn); // 设备编号
        byte[] msgId = ByteUtil.getBytes(Long.valueOf(messageId)); // msgId

        byte[] message = new byte[bytes.length + msgId.length + type.length + inc.length];

        ByteUtil.addBytes(message, bytes, 0);
        ByteUtil.addBytes(message, msgId, bytes.length);
        ByteUtil.addBytes(message, type, bytes.length + msgId.length);
        ByteUtil.addBytes(message, inc, bytes.length + msgId.length + type.length);

        messageHead.setMessage(message);
        return messageHead;
    }

    /**
     * 长度(4) + 设备编号(8) + messageId(8) + type(1) <hr>
     *     长度 = 设备编号(8) + messageId(8) + type(1) + body.length
     *     type: {@link BreakerProtocolType}
     * 断路器设备报文头
     * @see BreakerClientMessage
     * @return
     */
    public static DefaultMessageHead buildBreakerHeader(long deviceSn, int bodyLength, BreakerProtocolType type) {
        byte[] message = new byte[4 + 8 + 8 + 1];

        message[20] = type.code;
        String messageId = ClientSnGen.getMessageId();
        ByteUtil.addBytes(message, ByteUtil.getBytes(8 + 8 + 1 + bodyLength), 0);
        ByteUtil.addBytes(message, ByteUtil.getBytes(deviceSn), 4);
        ByteUtil.addBytes(message, ByteUtil.getBytes(Long.valueOf(messageId)), 12);

        return new DefaultMessageHead(deviceSn + "", messageId, type).build(message);
    }

    public static DefaultMessageHead buildBreakerHeader(byte[] message) {
        int length = ByteUtil.bytesToInt(message, 0);
        long deviceSn = ByteUtil.bytesToLong(message, 4);
        long messageId = ByteUtil.bytesToLong(message, 12);
        byte type = ByteUtil.getByte(message, 20);

        return new DefaultMessageHead(deviceSn + "", messageId + "", BreakerProtocolType.getInstance(type)).build(message);
    }

    public static DefaultMessageHead buildBreakerHeader(String deviceSn, String messageId, int bodyLength, BreakerProtocolType type) {
        byte[] message = new byte[4 + 8 + 8 + 1];

        message[20] = type.code;
        ByteUtil.addBytes(message, ByteUtil.getBytes(8 + 8 + 1 + bodyLength), 0);
        ByteUtil.addBytes(message, ByteUtil.getBytes(Long.valueOf(deviceSn)), 4);
        ByteUtil.addBytes(message, ByteUtil.getBytes(Long.valueOf(messageId)), 12);

        return new DefaultMessageHead(deviceSn + "", messageId + "", type).build(message);
    }

    /**
     * 断路器数据采集 报文体
     * 各字段增益 100
     * 电压(4) + 电流(4) + 有功功率(4) + 无功功率(4) + 功率因素(2)
     * @return
     */
    public static DefaultMessageBody buildBreakerBody() {
        byte[] message = new byte[18];
        int v = RandomUtil.randomInt(18000, 25000); // 电压
        int i = RandomUtil.randomInt(10, 5000); // 电流
        int power1 = RandomUtil.randomInt(100, 500000); // 有功功率
        int power2 = RandomUtil.randomInt(100, 500000);// 无功功率
        short py = (short)RandomUtil.randomInt(1, 100);// 功率因素

        ByteUtil.addBytes(message, ByteUtil.getBytes(v), 0);
        ByteUtil.addBytes(message, ByteUtil.getBytes(i), 4);
        ByteUtil.addBytes(message, ByteUtil.getBytes(power1), 8);
        ByteUtil.addBytes(message, ByteUtil.getBytes(power2), 12);
        ByteUtil.addBytes(message, ByteUtil.getBytes(py), 16);
        return new DefaultMessageBody(message);
    }

    public static DefaultMessageBody buildBreakerBody(StatusCode code) {
        byte[] message = new byte[4];
        ByteUtil.addBytes(message, ByteUtil.getBytes(code.code), 0);
        return new DefaultMessageBody(message);
    }

    public static DefaultMessageBody buildBreakerBody(byte[] message) {
        byte[] subBytes = ByteUtil.subBytes(message, 21);
        return new DefaultMessageBody(subBytes);
    }
}
