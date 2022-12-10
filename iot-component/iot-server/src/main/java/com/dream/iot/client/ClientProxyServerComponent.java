package com.dream.iot.client;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.proxy.ProxyClientType;
import com.dream.iot.proxy.ProxyServerMessage;
import com.dream.iot.server.component.LengthFieldBasedFrameDecoderServerComponent;
import com.dream.iot.server.protocol.HeartbeatProtocol;

import java.nio.ByteOrder;

public class ClientProxyServerComponent extends LengthFieldBasedFrameDecoderServerComponent<ProxyServerMessage> {

    public ClientProxyServerComponent(ConnectProperties connectProperties) {
        super(connectProperties, ByteOrder.LITTLE_ENDIAN, 5 * 1024, 0, 4, 0, 4, true);
    }

    @Override
    public String getName() {
        return "客户端代理服务";
    }

    @Override
    public AbstractProtocol getProtocol(ProxyServerMessage message) {
        ProxyClientType tradeType = message.getHead().getType();
        if(tradeType == ProxyClientType.Proxy_Client_Heart) {
            return HeartbeatProtocol.getInstance(message);
        } else {
            try {
                /**
                 * 此处设置执行状态为 null, 为了判断是否需要直接响应客户端还是等待拿到设备报文之后在响应客户端
                 * @see ClientProxyServerProtocol#doBuildResponseMessage()
                 */
                return new ClientProxyServerProtocol(message);
            } catch (Exception e) {
                // 直接响应失败状态给客户端
                return new ClientProxyServerProtocol(message).setFailEx(e).setExecStatus(ExecStatus.fail);
            }
        }
    }

    @Override
    public ProxyServerMessage createMessage(byte[] message) {
        return new ProxyServerMessage(message);
    }

    @Override
    public String getDesc() {
        return "用来代理应用客户端操作设备";
    }

}
