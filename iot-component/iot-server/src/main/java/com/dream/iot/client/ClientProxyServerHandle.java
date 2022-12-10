package com.dream.iot.client;

import com.dream.iot.consts.ExecStatus;
import com.dream.iot.proxy.ProxyClientMessageHead;
import com.dream.iot.proxy.ProxyServerMessage;
import com.dream.iot.server.ServerProtocolHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 对应的协议：
 * @see ClientProxyServerProtocol
 */
public class ClientProxyServerHandle implements ServerProtocolHandle<ClientProxyServerProtocol> {

    private ObjectProvider<ClientHandleFactory> handleFactory;
    private Logger logger = LoggerFactory.getLogger(ClientProxyServerHandle.class);

    public ClientProxyServerHandle(ObjectProvider<ClientHandleFactory> handleFactory) {
        this.handleFactory = handleFactory;
    }

    @Override
    public Object handle(ClientProxyServerProtocol protocol) {
        ProxyServerMessage serverMessage = protocol.requestMessage();

        try {
            ProxyClientMessageHead head = serverMessage.getHead();
            ClientHandleFactory handleFactory = this.handleFactory.getIfAvailable();

            /**
             * 如果处理完后:
             * 1. 如果返回了需要调用设备的协议, 先调用协议请求设备, 等待设备响应或在响应客户端
             * @see com.dream.iot.server.protocol.ServerInitiativeProtocol#proxyClientHandle(Object) 等待设备响应后, 会主动在给客户端调用请求, 响应请求信息
             *
             * 2. 如果返回 null, 则直接响应客户端
             */
            Object returnValue = handleFactory.getRelation(serverMessage);
            if(returnValue instanceof ClientProxySync) {
                ClientProxySync abstractProtocol = (ClientProxySync) returnValue;

                // 用来标记此协议是否是由客户端请求发起的
                abstractProtocol.setClientStart(true);
                long timeout = abstractProtocol.getTimeout();

                /**
                 * 1. 如果应用客户端指定了超时时间 >=0, 说明代理客户端需要等待客户端的响应
                 * 2. 如果代理客户端要调用的协议[abstractProtocol]不需要同步等待, 则直接响应代理客户端成功
                 */
                if(head.isWaiting()) {
                    /**
                     * @see com.dream.iot.server.protocol.ServerInitiativeProtocol#proxyClientHandle(Object)
                     */
                    abstractProtocol.setProxyProtocol(protocol);

                    // 如果指定的超时时间大于0
                    if(head.getTimeout() > 0) {
                        timeout = head.getTimeout();
                    }

                } else { // 无需等待设备返回, 直接返回代理客户端成功
                    protocol.setExecStatus(ExecStatus.success);
                }

                abstractProtocol.timeout(timeout);
                abstractProtocol.request();
            } else if(returnValue == null) {
                // 没有返回任何协议, 则直接响应客户端成功
                protocol.setExecStatus(ExecStatus.success);
            } else {
                // todo 未处理客户端代理返回
            }

        } catch (Exception e) { // 直接响应客户端失败, 以及异常信息
            protocol.setFailEx(e);
            protocol.setExecStatus(ExecStatus.fail);
            logger.error("处理应用程序客户端请求失败", e);
        }

        return null;
    }

}
