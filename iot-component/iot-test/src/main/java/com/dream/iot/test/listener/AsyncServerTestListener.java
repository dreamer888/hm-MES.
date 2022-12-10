package com.dream.iot.test.listener;

import com.dream.iot.FrameworkComponent;
import com.dream.iot.event.AsyncClientStatusEventListener;
import com.dream.iot.event.ClientStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端异步事件监听
 */
public class AsyncServerTestListener implements AsyncClientStatusEventListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean matcher(ClientStatusEvent event) {
        return event.getSource() instanceof String;
    }

    @Override
    public void offline(Object source, FrameworkComponent component) {
        logger.info("服务端异步事件监听({}) 掉线 - 客户端编号：{} - 线程：{} - 测试通过", component.getName(), source, Thread.currentThread().getName());
    }

    @Override
    public void online(Object source, FrameworkComponent component) {
        logger.info("服务端异步事件监听({}) 上线 - 客户端编号：{} - 线程：{} - 测试通过", component.getName(), source, Thread.currentThread().getName());
    }

}
