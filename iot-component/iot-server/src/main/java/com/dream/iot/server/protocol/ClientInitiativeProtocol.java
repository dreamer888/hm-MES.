package com.dream.iot.server.protocol;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.Message;
import com.dream.iot.ProtocolHandle;
import com.dream.iot.ProtocolType;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.ServerSocketProtocol;

/**
 * 首先此协议是一个服务端协议
 * @see ServerSocketProtocol
 * 其次此协议是由客户端主动请求服务端的协议
 * @param <M>
 */
public abstract class ClientInitiativeProtocol<M extends ServerMessage> extends ServerSocketProtocol<M> {

    public ClientInitiativeProtocol(M requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        this.buildRequestMessage();
        return this.exec(factory.getProtocolHandle(getClass()));
    }

    /**
     * 执行客户端请求业务
     * @param business 协议业务
     */
    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        //执行业务
        if(null != business) {
            business.handle(this);
        } else {
            if(logger.isTraceEnabled()) {
                logger.warn("客户端主动请求 没有指定[ServerProtocolHandle] - 协议类型: {} - 协议描述: {} - 说明: 不对此协议做业务处理", protocolType().getType(), desc());
            }
        }

        /**
         * 构建响应报文
         * 1. 有返回: 返回的报文将响应给客户端
         * 2. 无返回: 将不响应客户端
         */
        return buildResponseMessage();
    }

    /**
     * 构建响应报文
     * @see com.dream.iot.server.handle.ProtocolBusinessHandler
     * @return
     */
    public ClientInitiativeProtocol buildResponseMessage() {
        //对于客户端主动发起的请求,必须包含请求报文
        if(null == requestMessage()) {
            throw new IllegalStateException("不存在请求报文："+ requestMessage());
        }

        //创建响应报文
        responseMessage = doBuildResponseMessage();

        // 如果返回了响应报文, 说明需要写报文到客户端
        if(responseMessage != null) {
            return this;
        } else {
            // 注意：如果没有返回响应报文, 则无需响应给客户端
            return null;
        }
    }

    /**
     * 构建响应客户端请求的报文
     * @return 响应报文  如果返回值为空 则不响应给客户端
     */
    protected abstract M doBuildResponseMessage();

    /**
     * 对请求平台的报文进行报文解析
     * @return
     */
    public ClientInitiativeProtocol buildRequestMessage() {
        doBuildRequestMessage(this.requestMessage());

        Message.MessageHead messageHead = this.requestMessage().getHead();
        if(logger.isDebugEnabled() && messageHead != null) {
            logger.debug("客户端主动协议 构建请求报文 - 客户端编号: {} - 协议类型: {}" , getEquipCode(), protocolType());
        }

        return this;
    }

    /**
     * 解析构建客户端请求平台的报文
     * @param requestMessage
     */
    protected abstract void doBuildRequestMessage(M requestMessage);

    @Override
    public String desc() {
        return protocolType().getDesc();
    }

    @Override
    public abstract ProtocolType protocolType();

}
