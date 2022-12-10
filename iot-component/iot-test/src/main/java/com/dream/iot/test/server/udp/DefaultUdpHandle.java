package com.dream.iot.test.server.udp;

import com.dream.iot.server.udp.impl.DefaultUdpServerMessage;
import com.dream.iot.server.udp.impl.DefaultUdpServerProtocol;
import com.dream.iot.server.udp.impl.DefaultUdpServerProtocolHandle;
import org.springframework.stereotype.Component;

@Component
public class DefaultUdpHandle implements DefaultUdpServerProtocolHandle {

    @Override
    public Object handle(DefaultUdpServerProtocol protocol) {
        DefaultUdpServerMessage requestMessage = protocol.requestMessage();
        System.out.println("------------------------- 接收到UDP报文：" + new String(requestMessage.getMessage()));

        // 把报文原封不动响应(响应此请求)
        protocol.response(requestMessage.getMessage());

        // 主动向发送方发起一次请求, 报文不变
        DefaultUdpServerProtocol.write(requestMessage.getMessage(), requestMessage.getSender());
        return null;
    }
}
