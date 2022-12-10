package com.dream.iot.plc.siemens;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.plc.PlcException;

/**
 * 西门子S7编解码组件
 */
public class SiemensS7Component extends TcpClientComponent<SiemensS7Message> {

    public SiemensS7Component() { }

    public SiemensS7Component(SiemensConnectProperties config) {
        super(config);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        if(config instanceof SiemensConnectProperties) {
            return new SiemensS7Client(this, (SiemensConnectProperties) config);
        } else {
            throw new PlcException("不支持的西门子PLC连接配置 - 请使用[SiemensConnectProperties]");
        }
    }

    @Override
    public Class<SiemensS7Message> getMessageClass() {
        return SiemensS7Message.class;
    }

    @Override
    public String getName() {
        return "西门子S7序列";
    }

    @Override
    public String getDesc() {
        return getName();
    }

    @Override
    public AbstractProtocol getProtocol(SiemensS7Message message) {
        return remove(message.getMessageId());
    }
}
