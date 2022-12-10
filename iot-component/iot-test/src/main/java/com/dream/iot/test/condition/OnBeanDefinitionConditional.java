package com.dream.iot.test.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnBeanDefinitionConditional implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MergedAnnotation<ConditionalOnBeanDefinition> annotation = metadata.getAnnotations().get(ConditionalOnBeanDefinition.class);
        Class[] values = annotation.getValue("value", Class[].class).get();
        for (Class type : values) {
            String[] beanNamesForType = context.getBeanFactory().getBeanNamesForType(type);
            System.out.println(beanNamesForType);
        }
        //        context.getBeanFactory().containsBeanDefinition()
        return false;
    }
}
