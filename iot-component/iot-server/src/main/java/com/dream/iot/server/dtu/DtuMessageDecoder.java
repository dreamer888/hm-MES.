package com.dream.iot.server.dtu;

import com.dream.iot.*;
import com.dream.iot.codec.SocketMessageDecoder;
import com.dream.iot.server.IotSocketServer;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.dtu.message.DtuMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

/**
 * DTU设备报文解码
 * @param <M>
 */
public interface DtuMessageDecoder<M extends ServerMessage> extends SocketMessageDecoder<ByteBuf>
        , IotProtocolFactory<M>, IotSocketServer, DtuMessageAware<M> {

    DtuMessageAware<M> getDtuMessageAwareDelegation();

    @Override
    default DtuMessageType messageType() {
        return getDtuMessageAwareDelegation().messageType();
    }

    @Override
    default String resolveEquipCode(byte[] equipMessage) {
        return getDtuMessageAwareDelegation().resolveEquipCode(equipMessage);
    }

    @Override
    default String resolveHeartbeat(byte[] message) {
        // https://gitee.com/iteaj/iot/issues/I5GN72
        return getDtuMessageAwareDelegation().resolveHeartbeat(message);
    }

    @Override
    default AbstractProtocol customize(M message, IotProtocolFactory<M> factory) {
        return getDtuMessageAwareDelegation().customize(message, factory);
    }

    @Override
    default M decodeBefore(String equipCode, byte[] message, ByteBuf msg) {
        return getDtuMessageAwareDelegation().decodeBefore(equipCode, message, msg);
    }

    @Override
    default M createMessage(byte[] message) {
        SocketMessage serverMessage = SocketMessageDecoder.super.createMessage(message);
        if(serverMessage instanceof DtuMessage) {
            return (M) serverMessage;
        } else {
            throw new ProtocolException("Dtu报文对象必须是["+DtuMessage.class.getSimpleName()+"]的子类");
        }
    }

    @Override
    Class<M> getMessageClass();

    @Override
    default void doInitChannel(ChannelPipeline p) {
        IotSocketServer.super.doInitChannel(p);
        p.addBefore(CoreConst.SERVER_DECODER_HANDLER, "DtuFirstDeviceSnPackageHandler", new DtuFirstDeviceSnPackageHandler(this));
    }

    @Override
    default M doTcpDecode(ChannelHandlerContext ctx, ByteBuf in) {
        SocketMessage decode = SocketMessageDecoder.super.doTcpDecode(ctx, in);
        Object equipCode = ctx.channel().attr(CoreConst.EQUIP_CODE).get();

        // 设置Dtu报文的设备编号
        if(equipCode != null) {
            ((DtuMessage) decode).setEquipCode((String) equipCode);
        }

        return (M) decode;
    }

    default AbstractProtocol getProtocol(M message) {
        AbstractProtocol<M> protocol = customize(message, this);
        if(protocol != null) return protocol;

        // 获取自定义业务协议
        return doGetProtocol(message);
    }

    /**
     * 获取对应的协议
     * @param message
     * @return
     */
    AbstractProtocol doGetProtocol(M message);
}
