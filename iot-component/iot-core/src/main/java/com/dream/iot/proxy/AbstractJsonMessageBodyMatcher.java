package com.dream.iot.proxy;

import com.dream.iot.ProtocolException;
import com.dream.iot.client.ClientProxyRelation;

/**
 * 用于处理报文体是Json格式
 * @see ProxyClientJsonMessageBody
 */
public abstract class AbstractJsonMessageBodyMatcher implements AppClientTypeMatcherHandle<ProxyClientJsonMessageBody> {

    @Override
    public ProxyClientJsonMessageBody deserialize(Object body, RequestType type) {
        if(body != null) {
            if(type == RequestType.REQ) {
                if(body instanceof ProxyClientJsonMessageBody) {
                    ProxyClientJsonMessageBody jsonMessageBody = new ProxyClientJsonMessageBody();

                    return jsonMessageBody;
                } else {
                    throw new ProtocolException("报文体反序列化异常, AbstractJsonMessageBodyMatcher匹配器只支持Json格式的报文体类型: AppJsonMessageBody");
                }
            } else {

            }
        }

        return null;
    }

    @Override
    public abstract ClientProxyRelation handle(ProxyClientMessage<ProxyClientJsonMessageBody> message);
}
