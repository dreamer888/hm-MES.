package com.dream.iot.condition;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnBeanByOrConditional implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MergedAnnotation<ConditionalOnBeanByOr> annotation = metadata.getAnnotations().get(ConditionalOnBeanByOr.class);
        Class[] values = annotation.getValue("value", Class[].class).get();
        for (Class type : values) {
            try {
                String[] beanNamesForType = context.getBeanFactory().getBeanNamesForType(type);
                if(beanNamesForType.length != 0) {
                    return true;
                } else {
                    continue;
                }
            } catch (BeansException e) {
                continue;
            }
        }

        return false;
    }
}
