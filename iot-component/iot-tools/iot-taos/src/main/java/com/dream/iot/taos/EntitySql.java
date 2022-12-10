package com.dream.iot.taos;

import org.springframework.jdbc.core.SqlParameterValue;

import java.util.List;

public class EntitySql {

    private String tableName;

    private List<SqlParameterValue> values;

    public EntitySql(String tableName, List<SqlParameterValue> values) {
        this.tableName = tableName;
        this.values = values;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SqlParameterValue> getValues() {
        return values;
    }

    public void setValues(List<SqlParameterValue> values) {
        this.values = values;
    }
}
