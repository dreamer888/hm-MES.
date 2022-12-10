package com.dream.iot.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeanByOrConditional.class)
public @interface ConditionalOnBeanByOr {

    Class<?>[] value() default {};
}
