package com.dream.iot.protocol.socket;

import com.dream.iot.SocketMessage;
import com.dream.iot.AbstractProtocol;
import com.dream.iot.BusinessAction;

/**
 * create time: 2021/8/8
 *  用来声明此协议是一个socket协议 如：tcp, udp, smtp等
 * @author dream
 * @since 1.0
 */
public abstract class AbstractSocketProtocol<M extends SocketMessage>
        extends AbstractProtocol<M> implements BusinessAction {

}
