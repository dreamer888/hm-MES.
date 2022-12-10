package com.dream.iot.client.protocol;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.Message;
import com.dream.iot.ProtocolHandle;
import com.dream.iot.ProtocolType;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.client.ClientMessage;

/**
 * 首先它是一个客户端协议
 * 其次它属于服务端主动请求客户端的协议
 * @param <C>
 */
public abstract class ServerInitiativeProtocol<C extends ClientMessage> extends ClientSocketProtocol<C>{

    public ServerInitiativeProtocol(C requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    public AbstractProtocol buildRequestMessage() {
        doBuildRequestMessage(this.requestMessage());

        Message.MessageHead messageHead = this.requestMessage().getHead();
        if(logger.isDebugEnabled() && messageHead != null) {
            logger.debug("服务端主动请求 构建请求报文 - 客户端编号: {} - 协议类型: {}" , getEquipCode(), protocolType());
        }

        return this;
    }

    /**
     * 解析构建客户端请求平台的报文
     * @param requestMessage
     */
    protected abstract void doBuildRequestMessage(C requestMessage);

    @Override
    public AbstractProtocol buildResponseMessage() {
        //对于服务端主动发起的请求,必须包含请求报文
        if(null == requestMessage()) {
            throw new IllegalStateException("不存在请求报文："+ requestMessage());
        }

        //创建响应报文
        responseMessage = doBuildResponseMessage();

        // 如果返回了响应报文, 说明需要响应给服务端
        if(responseMessage != null) {
            return this;
        } else {
            // 注意：如果没有返回响应报文, 则无需响应给客户端
            return null;
        }
    }

    /**
     * 构建响应服务端的报文
     * @return 响应报文  如果返回值为空 则不响应给服务端
     */
    protected abstract C doBuildResponseMessage();

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        return this.exec(factory.getProtocolHandle(getClass()));
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle handle) {
        //执行业务
        if(null != handle) {
            handle.handle(this);
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("服务端主动请求 没有指定业务处理器 - 协议类型: {} - 说明: 此协议将无业务处理", protocolType());
            }
        }

        /**
         * 构建响应报文
         * 1. 有返回: 返回的报文将响应给客户端
         * 2. 无返回: 将不响应客户端
         */
        return buildResponseMessage();
    }

    @Override
    public abstract ProtocolType protocolType();
}
