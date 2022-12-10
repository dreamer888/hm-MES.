package com.dream.iot.taos;

import org.springframework.jdbc.core.SqlParameterValue;

public class SqlExecContext {

    /**
     * 记录数
     */
    private int count;

    /**
     * 还未入库条数
     */
    private int remain;

    private StringBuilder sql;

    private SqlParameterValue[] values;


    public SqlExecContext(StringBuilder sql, SqlParameterValue[] values, int count, int remain) {
        this.sql = sql;
        this.count = count;
        this.remain = remain;
        this.values = values;
    }

    public StringBuilder getSql() {
        return sql;
    }

    public void setSql(StringBuilder sql) {
        this.sql = sql;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public SqlParameterValue[] getValues() {
        return values;
    }

    public void setValues(SqlParameterValue[] values) {
        this.values = values;
    }
}
