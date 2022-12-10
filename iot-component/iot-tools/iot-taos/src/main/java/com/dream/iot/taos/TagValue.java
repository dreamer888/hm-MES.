package com.dream.iot.taos;

public class TagValue {

    private Object value;

    /**
     * @see java.sql.Types
     */
    private int type;

    public TagValue(Object value) {
        this.value = value;
    }

    public TagValue(Object value, int type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
