package com.dream.iot.server.dtu;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.IotProtocolFactory;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * 过滤Dtu设备特有的报文, 比如心跳包或者AT指令
 */
public interface DtuMessageAware<M extends ServerMessage> {

    /**
     * 心跳包以及注册包的格式
     * @return
     */
    default DtuMessageType messageType() {
        return DtuMessageType.ASCII;
    }

    /**
     * 解析默认心跳报文
     * @param message
     * @return
     */
    default String resolveHeartbeat(byte[] message) {
        if(messageType() == DtuMessageType.ASCII) {
            return new String(message, StandardCharsets.US_ASCII);
        } else {
            return ByteUtil.bytesToHex(message);
        }
    }

    /**
     * 解析第一帧的报文到设编号
     * @param equipMessage
     * @return
     */
    default String resolveEquipCode(byte[] equipMessage) {
        if(messageType() == DtuMessageType.ASCII) {
            return new String(equipMessage, StandardCharsets.US_ASCII);
        } else {
            return ByteUtil.bytesToHex(equipMessage);
        }
    }

    /**
     * 自定义协议
     * @param message {@link #decodeBefore(String, byte[], ByteBuf)} 返回值
     * @param factory 协议工厂
     * @return 返回自定义协议
     */
    AbstractProtocol customize(M message, IotProtocolFactory<M> factory);

    /**
     * 在下一个解码器解析之前调用
     * @param equipCode 设备编号
     * @param message 已经读取的报文 {@link ByteBuf#slice()}
     * @param msg 源报文对象
     * @return return null会继续解码 而如果return M不继续解码直接做业务处理
     */
    M decodeBefore(String equipCode, byte[] message, ByteBuf msg);

}
