package com.dream.iot.taos;

/**
 * 使用于在插入数据时自动创建数据表的情况
 * e.g: INSERT INTO d21001 USING meters TAGS ('Beijing.Chaoyang', 2)
 * @see STable#tagsResolver()
 */
public interface TagsResolver {

    /**
     * 解析出tags的值, 此值最好使用缓存提升性能
     * @param tagName = {@link STable#tags()} 返回值
     * @param tableName = {@link STable#table()}返回值
     * @return 如果返回null则此tag将不加入插入语句，taos数据库将默认设置此tag值为null
     */
    TagValue resolve(String tableName, String tagName);
}
