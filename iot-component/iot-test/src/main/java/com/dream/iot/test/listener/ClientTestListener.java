package com.dream.iot.test.listener;

import com.dream.iot.FrameworkComponent;
import com.dream.iot.client.IotClient;
import com.dream.iot.client.SocketClient;
import com.dream.iot.event.ClientStatusEvent;
import com.dream.iot.event.ClientStatusEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端上下线同步事件监听
 */
public class ClientTestListener implements ClientStatusEventListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean matcher(ClientStatusEvent event) {
        return event.getSource() instanceof IotClient;
    }

    @Override
    public void offline(Object source, FrameworkComponent component) {
        SocketClient client = (SocketClient) source;
        logger.info("客户端同步事件监听({}) 掉线 - 客户端：{} - 测试通过", component.getName(), client.getConfig().connectKey());
    }

    @Override
    public void online(Object source, FrameworkComponent component) {
        SocketClient client = (SocketClient) source;
        logger.info("客户端同步事件监听({}) 上线 - 客户端：{} - 测试通过", component.getName(), client.getConfig().connectKey());
    }

}
