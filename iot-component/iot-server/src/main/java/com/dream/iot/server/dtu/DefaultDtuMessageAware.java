package com.dream.iot.server.dtu;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.IotProtocolFactory;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.dtu.message.DtuMessage;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class DefaultDtuMessageAware<M extends ServerMessage> implements DtuMessageAware<M> {

    private DtuMessageType messageType;
    private DtuMessageDecoder<M> decoder;

    public DefaultDtuMessageAware() {
        this(DtuMessageType.ASCII);
    }

    public DefaultDtuMessageAware(DtuMessageType messageType) {
        this.messageType = messageType;
    }

    public DefaultDtuMessageAware(DtuMessageDecoder decoder) {
        this(DtuMessageType.ASCII);
        this.decoder = decoder;
    }

    @Override
    public DtuMessageType messageType() {
        return this.messageType;
    }

    /**
     * 自定义协议
     * @param message {@link #decodeBefore(String, byte[], ByteBuf)} 返回值
     * @param factory 协议工厂
     * @return 返回自定义协议
     */
    public AbstractProtocol customize(M message, IotProtocolFactory<M> factory) {
        DefaultMessageHead head = (DefaultMessageHead) message.getHead();

        // Dtu获取设备编号的报文使用默认的协议来处理{@link DtuDeviceSnProtocol}
        if(head != null) {
            // 设备编号注册
            if(head.getType() == DtuCommonProtocolType.DEVICE_SN) {
                // DTU设备注册包
                return this.getDeviceSnRegisterProtocol(message);
            } else if(head.getType() == DtuCommonProtocolType.HEARTBEAT) {
                // DTU心跳包
                return this.getHeartbeatProtocol(message);
            } else if(head.getType() == DtuCommonProtocolType.AT) {
                // DTU AT协议
                return (AbstractProtocol) factory.remove(head.getMessageId());
            } else if(head.getType() == DtuCommonProtocolType.DTU) {
                // DTU私有协议
                return this.getDtuPrivateProtocol(message);
            }
        }

        return null;
    }

    /**
     * 返回DTU设备的私有协议
     * @param message
     * @return
     */
    private AbstractProtocol getDtuPrivateProtocol(M message) {
        return new DtuPrivateProtocol(message);
    }

    /**
     * 返回DTU设备注册设备编号协议
     * @param message
     * @return
     */
    protected AbstractProtocol getDeviceSnRegisterProtocol(M message) {
        return new DtuDeviceSnProtocol(message);
    }

    /**
     * 返回DTU设备心跳协议
     * @param message
     * @return
     */
    protected AbstractProtocol getHeartbeatProtocol(M message) {
        return new DtuHeartbeatProtocol(message);
    }

    /**
     * 通过设备编号获取心跳包文的长度
     * @param equipCode 心跳包和注册包必须一致
     * @return
     */
    // https://gitee.com/iteaj/iot/issues/I5HO2L
    protected int heartbeatLength(String equipCode) {
        DtuMessageType messageType = getDecoder().messageType();
        if(messageType == DtuMessageType.ASCII) {
            return equipCode.getBytes(StandardCharsets.US_ASCII).length;
        } else {
            return ByteUtil.hexToByte(equipCode).length;
        }
    }

    /**
     * 在下一个解码器解析之前调用
     * @param equipCode 设备编号
     * @param message 已经读取的报文 {@link ByteBuf#slice()}
     * @param msg 源报文对象
     * @return return null会继续解码 而如果return M不继续解码直接做业务处理
     */
    public M decodeBefore(String equipCode, byte[] message, ByteBuf msg) {
        // https://gitee.com/iteaj/iot/issues/I5GN72
        String strMsg = getDecoder().resolveHeartbeat(message);
        // 以设备编号开头则说明包含的部分就是心跳报文
        if(strMsg.startsWith(equipCode)) {
            message = new byte[this.heartbeatLength(equipCode)];
            msg.readBytes(message); // 读取设备编号长度的报文

            M heartMessage = createMessage(message);
            if(heartMessage instanceof DtuMessage) {
                ((DtuMessage) heartMessage).setEquipCode(equipCode);

                // 心跳协议
                ((DtuMessage) heartMessage).setProtocolType(DtuCommonProtocolType.HEARTBEAT);
            }

            return heartMessage;
        }

        // 如果是At指令的响应
        byte[] atMsg; M customizeMessage;
        if((atMsg = readAtMsg(equipCode, message, msg)) != null) { // 需要自定义读取多少长度报文
            M atMessage = createMessage(atMsg);
            if(atMessage instanceof DtuMessage) {
                ((DtuMessage) atMessage).setEquipCode(equipCode);
                ((DtuMessage) atMessage).setProtocolType(DtuCommonProtocolType.AT);
            }

            return atMessage;
            // 自定义协议处理, 默认读取所有报文
        } else if((customizeMessage = customizeType(equipCode, message, msg)) != null) {
            if(((DtuMessage) customizeMessage).getEquipCode() == null) {
                ((DtuMessage) customizeMessage).setEquipCode(equipCode);
            }

            return customizeMessage;
        }

        return null;
    }

    protected M createMessage(byte[] message) {
        return getDecoder().createMessage(message);
    }

    protected M createMessage(DtuCommonProtocolType type, ByteBuf msg) {
        byte[] message = new byte[msg.readableBytes()]; // 读取所有可读的报文
        // modbus的报文有modbus handle处理 这边不读取
        if(type != DtuCommonProtocolType.PASSED) {
            msg.readBytes(message);
            return (M) ((DtuMessage) createMessage(message)).setProtocolType(type);
        }

        return (M) ((DtuMessage) createMessage(message)).setProtocolType(type);
    }

    /**
     * 是否是AT指令报文
     * @param message
     * @return 返回的数据需要从 {@param msg}重新读取
     */
    protected byte[] readAtMsg(String equipCode, byte[] message, ByteBuf msg) {
        return null;
    }

    /**
     * 自定义类型
     * @param equipCode 设备编号
     * @param message  未读数据的备份
     * @param msg 需要读取的报文
     * @return 返回null 表示等待下一个报文 返回{@link DtuCommonProtocolType#PASSED}表示交由下一个解码器解码
     */
    protected M customizeType(String equipCode, byte[] message, ByteBuf msg) {
        return createMessage(DtuCommonProtocolType.PASSED, msg);
    }

    public DtuMessageDecoder<M> getDecoder() {
        return decoder;
    }

    public void setDecoder(DtuMessageDecoder<M> decoder) {
        this.decoder = decoder;
    }
}
