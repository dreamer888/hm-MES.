package com.dream.iot.client.handle;

import com.dream.iot.proxy.ProxyClientMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * create time: 2021/3/5
 *  用来声明此方法再注入参数的时候注入的对象是报文的body
 *  1. 如果是实体类则直接反序列化后注入
 *  2. 如果是Json对象则直接注入json对象
 * @see ProxyClientMessage#getBody()
 * @author dream
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IotBody {

}
