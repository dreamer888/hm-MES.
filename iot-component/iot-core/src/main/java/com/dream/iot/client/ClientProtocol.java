package com.dream.iot.client;

import com.dream.iot.Protocol;
import com.dream.iot.ProtocolException;

/**
 * create time: 2021/8/8
 *  客户端协议
 * @author dream
 * @since 1.0
 */
public interface ClientProtocol<M extends ClientMessage> extends Protocol {

    @Override
    M requestMessage();

    @Override
    M responseMessage();

    /**
     * 获取组件默认的客户端
     * @return
     */
    IotClient getIotClient() throws ProtocolException;
}
