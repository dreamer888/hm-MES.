package com.dream.iot.tools.annotation;

import java.lang.reflect.Field;

public class IotFieldMeta {

    private Field field;

    private String name;

    private IotField iotField;

    public IotFieldMeta(IotField iotField, String name, Field field) {
        this.field = field;
        this.name = name;
        this.iotField = iotField;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public IotField getIotField() {
        return iotField;
    }

    public void setIotField(IotField iotField) {
        this.iotField = iotField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
