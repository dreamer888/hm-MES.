package com.dream.iot.server.protocol;

import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolType;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.protocol.CommonProtocolType;
import com.dream.iot.server.message.LocalLoopMessage;

import java.io.IOException;

/**
 * <p>本地到本地的协议,此协议平台不向外发送报文
 * ,也不会有设备请求此协议的报文</p>
 *
 * @see LocalLoopMessage  此协议使用的报文
 * @author dream
 * @since 1.8
 */
public class LocalLoopProtocol extends ServerInitiativeProtocol<LocalLoopMessage> {

    private String deviceSn;

    @Override
    public void request() throws ProtocolException {
        this.setExecStatus(ExecStatus.success);
        if(this.isSyncRequest()) {
            this.exec(this.getProtocolHandle());
        } else {
            this.exec(this.getProtocolHandle());
        }
    }

    @Override
    protected LocalLoopMessage doBuildRequestMessage() throws IOException {
        LocalLoopMessage.LocalLoopHead head = new LocalLoopMessage.LocalLoopHead(this.deviceSn);
        return new LocalLoopMessage(head);
    }

    @Override
    protected void doBuildResponseMessage(LocalLoopMessage message) {

    }

    @Override
    public Object relationKey() {
        throw null;
    }

    @Override
    public boolean isRelation() {
        return false;
    }

    @Override
    public ProtocolType protocolType() {
        return CommonProtocolType.LocalLoop;
    }

    @Override
    public String desc() {
        return "本地回环协议";
    }
}
