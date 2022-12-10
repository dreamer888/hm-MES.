package com.dream.iot.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表标签配置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface IotTag {

    /**
     * 表标签名, 默认实体类字段名
     * @return
     */
    String value() default "";
}
