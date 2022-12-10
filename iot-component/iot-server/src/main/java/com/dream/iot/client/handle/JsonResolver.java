package com.dream.iot.client.handle;

import com.alibaba.fastjson.util.TypeUtils;
import com.dream.iot.client.ParamResolver;
import com.dream.iot.proxy.ProxyServerMessage;
import com.dream.iot.proxy.ProxyClientMessage;
import com.dream.iot.proxy.ProxyClientJsonMessageBody;
import com.dream.iot.proxy.ProxyClientMessageBody;

/**
 * create time: 2021/3/4
 *
 * @author dream
 * @since 1.0
 */
public class JsonResolver implements ParamResolver {

    private static final String DEVICE_SN = "deviceSn";

    @Override
    public String getDeviceSn(ProxyClientMessage message) {
        ProxyClientMessageBody body = message.getBody();
        return body.getDeviceSn();
    }

    @Override
    public String getTradeType(ProxyClientMessage message) {
        return (String) message.getBody().getCtrl();
    }

    /**
     * 1. 如果名称为deviceSn则返回设备编号
     * 2. 不支持解析非包装的基本数据类型 比如：int, double 请使用Integer类型替代
     * 3. 如果名称为body则直接返回
     * @param name
     * @param type
     * @param message
     * @return
     */
    @Override
    public Object resolver(String name, Class type, ProxyServerMessage message) {
        if(type == String.class && DEVICE_SN.equals(name)) {
            return message.getDeviceSn();
        }

        if(message.getBody() instanceof ProxyClientJsonMessageBody) {
            try {
                return this.bodyTypeParamResolver(name, type, (ProxyClientJsonMessageBody)message.getBody());
            } catch (Exception e) {
                throw new IllegalArgumentException("不能解析名为["+name+"]的参数["+type+"]");
            }
        } else {
            throw new UnsupportedOperationException("只支持报文体类型[ProxyClientJsonMessageBody]");
        }
    }

    private Object bodyTypeParamResolver(String name, Class type, ProxyClientJsonMessageBody body) {
        return TypeUtils.castToJavaBean(body.value(name), type);
    }
}
