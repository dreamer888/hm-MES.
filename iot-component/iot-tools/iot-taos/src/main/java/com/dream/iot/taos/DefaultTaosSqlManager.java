package com.dream.iot.taos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTaosSqlManager implements TaosSqlManager, BeanFactoryAware, InitializingBean {

    private BeanFactory beanFactory;

    private JdbcTemplate defaultJdbcTemplate;

    private TaosSqlContextScheduler taskScheduler;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<Class<?>, SqlContext> sqlContextMap = new ConcurrentHashMap<>(16);

    @Override
    public EntityMetas getEntityMeta(Class entityClass) {
        SqlContext sqlContext = sqlContextMap.get(entityClass);
        if(sqlContext == null) {
            synchronized (this) {
                final EntityMetas entityMetas = new EntityMetas(entityClass).resolve(this.beanFactory);
                if(entityClass == null) {
                    throw new TaosException("["+TaosHandle.class.getSimpleName()+"]对象的返回实体对象必须使用"+STable.class.getSimpleName()+"注解");
                }

                sqlContext = new SqlContext(entityMetas);
                sqlContextMap.put(entityClass, sqlContext);
            }
        }

        return sqlContext.getMeta();
    }

    @Override
    public SqlContext getSqlContext(Class entityClass) {
        SqlContext sqlContext = sqlContextMap.get(entityClass);
        if(sqlContext == null) {
            this.getEntityMeta(entityClass);
        }

        return sqlContextMap.get(entityClass);
    }

    @Override
    public SqlContext addEntity(Object entity) {
        SqlContext sqlContext = this.getSqlContext(entity.getClass());

        final String tableName = sqlContext.getTableName(entity);

        // 解析参数
        List<SqlParameterValue> params = sqlContext.getMeta().getParams(entity);
        return sqlContext.addEntitySql(tableName, params);
    }

    @Override
    public SqlContext addEntity(Class entityClass, List<Object> entities) {
        SqlContext sqlContext = this.getSqlContext(entityClass);

        entities.forEach(entity -> {
            String value = sqlContext.getTableName(entity);

            // 解析普通字段参数
            List<SqlParameterValue> params = sqlContext.getMeta().getParams(entity);

            sqlContext.addEntitySql(new EntitySql(value, params));
        });

        return sqlContext;
    }

    @Override
    public int update(Object entity) {
        SqlContext sqlContext = this.getSqlContext(entity.getClass());

        final SqlExecContext execContext = sqlContext.getExecContext(entity);
        return sqlContext.doUpdate(execContext, defaultJdbcTemplate);
    }

    @Override
    public int batchUpdate(Class entityClazz, List<Object> entities) {
        final SqlContext sqlContext = this.getSqlContext(entityClazz);

        Collection<EntitySql> entitySqls = new ArrayList<>(entities.size());
        entities.forEach(entity -> {
            String value = sqlContext.getTableName(entity);

            // 解析普通字段参数
            List<SqlParameterValue> params = sqlContext.getMeta().getParams(entity);

            entitySqls.add(new EntitySql(value, params));
        });

        final SqlExecContext execContext = sqlContext.getExecContext(entitySqls, 0);
        return sqlContext.doUpdate(execContext, defaultJdbcTemplate);
    }

    @Override
    public int batchUpdate(Class entityClazz, List<Object> entities, int size) {
        if(CollectionUtils.isEmpty(entities)) {
            return 0;
        } else if(entities.size() < size) {
            return batchUpdate(entityClazz, entities);
        } else {
            int updateIndex = 0;
            int index = entities.size() / size;
            int remain = entities.size() % size;
            index = remain == 0 ? index : index + 1;
            List<Object> objects;
            for(int i=0; i < index; i++) {
                if(i == index - 1) { // 最后一次
                    objects = entities.subList(i * size, entities.size());
                } else {
                    objects = entities.subList(i * size, i * size + size);
                }

                updateIndex += this.batchUpdate(entityClazz, objects);
            }

            return updateIndex;
        }
    }

    @Override
    public void execute(Object entity, TaosHandle handle) {
        // 定时批量写入
        if(handle.maxOfPeer() > 1) {
            SqlContext sqlContext = this.addEntity(entity);

            // 初始化SqlContext对象
            if(sqlContext.getTaosHandle() == null) {
                // 确保只初始化一次
                synchronized (sqlContext) {
                    if(sqlContext.getTaosHandle() == null) {
                        sqlContext.setTaosHandle(handle);

                        // 加入任务调度
                        taskScheduler.scheduler(sqlContext);
                    }

                }

                sqlContext.setJdbcTemplate(beanFactory.getBean(handle.jdbcTemplate(), JdbcTemplate.class));
            }
        } else { // 实时写入
            SqlContext sqlContext = this.getSqlContext(entity.getClass());
            JdbcTemplate jdbcTemplate = sqlContext.getJdbcTemplate();

            if(jdbcTemplate == null) {
                jdbcTemplate = beanFactory.getBean(handle.jdbcTemplate(), JdbcTemplate.class);
                sqlContext.setTaosHandle(handle);
                sqlContext.setJdbcTemplate(jdbcTemplate);
            }

            sqlContext.update(entity);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.taskScheduler = beanFactory.getBean(TaosSqlContextScheduler.class);
        this.defaultJdbcTemplate = beanFactory.getBean("jdbcTemplate", JdbcTemplate.class);
    }
}
