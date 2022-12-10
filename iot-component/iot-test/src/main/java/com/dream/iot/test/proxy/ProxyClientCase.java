package com.dream.iot.test.proxy;

import com.dream.iot.client.ClientProperties;
import com.dream.iot.client.proxy.ProxyClientProtocol;
import com.dream.iot.proxy.ProxyClientJsonMessageBody;
import com.dream.iot.proxy.ProxyClientMessage;
import com.dream.iot.proxy.ProxyClientMessageHead;
import com.dream.iot.test.IotTestHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;

public class ProxyClientCase implements IotTestHandle {

    @Autowired
    private ClientProperties properties;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Override
    public void start() {
        scheduler.schedule(() -> {
            // 代理客户端参数解析测试
            ProxyClientMessage message = new ProxyClientMessage(new ProxyClientMessageHead()
                    , new ProxyClientJsonMessageBody("123456", "ctrl.test.param").add("status", "online"));
            new ProxyClientProtocol(message).request();

            // 代理客户端代理协议测试
            ProxyClientMessage sync = new ProxyClientMessage(new ProxyClientMessageHead(-1)
                    , new ProxyClientJsonMessageBody("123456", "ctrl.test.sync"));
            new ProxyClientProtocol(sync).request(protocol -> null);
        }, Instant.now().plusSeconds(15));
    }

    @Override
    public int getOrder() {
        return 1000 * 1000;
    }
}
