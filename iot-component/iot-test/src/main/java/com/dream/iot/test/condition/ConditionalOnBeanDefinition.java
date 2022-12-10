package com.dream.iot.test.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeanDefinitionConditional.class)
public @interface ConditionalOnBeanDefinition {

    Class<?>[] value() default {};
}
