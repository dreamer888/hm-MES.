package com.dream.iot.server.message;

import com.dream.iot.Message;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.protocol.CommonProtocolType;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.protocol.LocalLoopProtocol;

import java.util.UUID;

/**
 * <p>本地回环报文</p>
 * @see LocalLoopProtocol  使用此报文的协议基类
 * Create Date By 2017-09-29
 * @author dream
 * @since 1.7
 */
public class LocalLoopMessage extends ServerMessage {

    private Object param; //本地回环参数
    private String deviceSn;

    public LocalLoopMessage(byte[] message) {
        super(message);
    }

    public LocalLoopMessage(LocalLoopHead head) {
        super(head);
    }

    public LocalLoopMessage(LocalLoopHead head, LocalLoopBody body) {
        super(head, body);
    }

    @Override
    protected DefaultMessageHead doBuild(byte[] message) {
        return null;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public static class LocalLoopHead implements Message.MessageHead {
        private String equipCode;
        private String messageId;

        public LocalLoopHead(String equipCode) {
            this(equipCode, UUID.randomUUID().toString());
        }

        public LocalLoopHead(String equipCode, String messageId) {
            this.equipCode = equipCode;
            this.messageId = messageId;
        }

        @Override
        public String getEquipCode() {
            return this.equipCode;
        }

        @Override
        public void setEquipCode(String equipCode) {
            this.equipCode = equipCode;
        }

        @Override
        public String getMessageId() {
            return this.messageId;
        }

        @Override
        public Object getType() {
            return CommonProtocolType.LocalLoop;
        }

        @Override
        public byte[] getMessage() {
            return Message.EMPTY;
        }
    }

    public class LocalLoopBody implements Message.MessageBody {

        @Override
        public byte[] getMessage() {
            return Message.EMPTY;
        }
    }
}
