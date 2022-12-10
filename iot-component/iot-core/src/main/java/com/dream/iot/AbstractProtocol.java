package com.dream.iot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Create Date By 2017-09-12
 * @author dream
 * @since 1.7
 */
public abstract class AbstractProtocol<M extends SocketMessage> implements Protocol {

    /** 请求报文(客户端请求平台或者是平台请求客户端) */
    protected M requestMessage;

    /** 响应报文(客户端响应平台或者平台响应客户端) */
    protected M responseMessage;

    /** 自定义参数 */
    private Map<String, Object> param;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractProtocol() { }

    @Override
    public M requestMessage() {
        return requestMessage;
    }

    @Override
    public M responseMessage() {
        return responseMessage;
    }

    /**
     * 构建请求报文
     * @return
     */
    public abstract AbstractProtocol buildRequestMessage();

    /**
     * 构建响应报文
     * @return
     */
    public abstract AbstractProtocol buildResponseMessage();

    @Override
    public String getEquipCode() {
        if(requestMessage() != null && requestMessage().getHead() != null) {
            return requestMessage().getHead().getEquipCode();
        } else if(responseMessage() != null && responseMessage().getHead() != null) {
            return responseMessage().getHead().getEquipCode();
        }

        return null;
    }

    @Override
    public String desc() {
        return "";
    }

    public AbstractProtocol addParam(String key, Object value){
        if(CollectionUtils.isEmpty(param))
            param = new HashMap<>();

        param.put(key, value);

        return this;
    }

    public AbstractProtocol removeParam(String key){
        if(CollectionUtils.isEmpty(param))
            return this;

        param.remove(key);
        return this;
    }

    public Object getParam(String key){
        if(CollectionUtils.isEmpty(param))
            return null;

        return param.get(key);
    }

    public void setRequestMessage(M requestMessage) {
        this.requestMessage = requestMessage;
    }

    public <T extends AbstractProtocol> T setResponseMessage(M responseMessage) {
        this.responseMessage = responseMessage;
        return (T) this;
    }
}
