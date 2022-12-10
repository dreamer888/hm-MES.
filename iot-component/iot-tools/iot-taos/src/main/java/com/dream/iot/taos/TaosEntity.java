package com.dream.iot.taos;

/**
 * taos操作实体增强
 */
public interface TaosEntity extends TagsResolver {

    /**
     * @see STable#table() 优先级大于此注解
     * @return 返回数据表表名
     */
    String getDataTable();

    // 解析tags
    @Override
    default TagValue resolve(String tableName, String tagName) {
        return null;
    }
}
