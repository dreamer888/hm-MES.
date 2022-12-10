package com.dream.iot.taos;

import java.util.List;

public interface TaosSqlManager {

    /**
     * 解析实体参数到插入sql语句
     * @param entityClass
     * @return 返回insert sql
     */
    EntityMetas getEntityMeta(Class entityClass);

    /**
     * 获取sql上下文
     * @param entityClass
     * @return
     */
    SqlContext getSqlContext(Class entityClass);

    /**
     * 新增实体到sql上下文
     * @param entity
     * @return
     */
    SqlContext addEntity(Object entity);

    /**
     * 批量新增实体到sql上下文
     * @param entityClass
     * @param entities 所有的对象必须是 {@code entityClass}的实例
     * @return
     */
    SqlContext addEntity(Class entityClass, List<Object> entities);

    /**
     * 单条数据入库
     * @param entity
     * @return
     */
    int update(Object entity);

    /**
     * 批量数据入库
     * @param entityClazz
     * @param entities
     * @return
     */
    int batchUpdate(Class entityClazz, List<Object> entities);


    /**
     * 批量数据入库
     * @param entityClazz
     * @param entities
     * @param size 每次入库条数
     * @return
     */
    int batchUpdate(Class entityClazz, List<Object> entities, int size);

    /**
     * 执行
     * @param entity
     * @param handle
     */
    void execute(Object entity, TaosHandle handle);
}
