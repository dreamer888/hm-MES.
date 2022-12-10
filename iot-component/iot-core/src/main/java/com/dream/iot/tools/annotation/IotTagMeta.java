package com.dream.iot.tools.annotation;

import java.lang.reflect.Field;

public class IotTagMeta {

    private String name;

    private IotTag tag;

    private Field field;

    public IotTagMeta(String name, IotTag tag, Field field) {
        this.tag = tag;
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IotTag getTag() {
        return tag;
    }

    public void setTag(IotTag tag) {
        this.tag = tag;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
