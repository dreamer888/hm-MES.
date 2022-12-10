package com.dream.iot.taos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class SqlContext implements Runnable {

    private EntityMetas meta;

    private TaosHandle taosHandle;

    private JdbcTemplate jdbcTemplate;

    private TruncateLinkedList<EntitySql> entitySqls;

    private static final String Insert = "insert into ";
    private static ExpressionParser parser = new SpelExpressionParser();
    private static Logger logger = LoggerFactory.getLogger(SqlContext.class);

    public SqlContext(EntityMetas meta) {
        this.meta = meta;
        this.entitySqls = new TruncateLinkedList<>();
    }

    public synchronized SqlContext addEntitySql(EntitySql entitySql) {
        this.entitySqls.addLast(entitySql);
        return this;
    }

    public synchronized SqlContext addEntitySql(Collection collection) {
        this.entitySqls.addAll(collection);
        return this;
    }

    public synchronized SqlContext addEntitySql(String tableName, List<SqlParameterValue> values) {
        return this.addEntitySql(new EntitySql(tableName, values));
    }

    /**
     * 批量任务
     * @return
     */
    public SqlExecContext getExecContext(Collection<EntitySql> execEntitySqls, int remain) {
        if (CollectionUtils.isEmpty(execEntitySqls)) {
            return null;
        }

        StringBuilder sb = new StringBuilder(Insert);
        EntityMetas meta = this.getMeta();

        Map<String, List<EntitySql>> stringListMap = new HashMap<>();
        List<SqlParameterValue> parameterValues = new ArrayList<>();

        execEntitySqls.forEach(item -> {
            List<EntitySql> entitySqls = stringListMap.get(item.getTableName());
            if (entitySqls == null) {
                stringListMap.put(item.getTableName(), entitySqls = new ArrayList<>());
            }

            entitySqls.add(item);
        });

        stringListMap.forEach((tableName, entitySqls) -> {
            sb.append(tableName);
            if (meta.getTagInsertSql() != null) {
                sb.append(" ").append(meta.getTagInsertSql()).append(" tags ").append(meta.getTagParamSql());
                List<SqlParameterValue> tagParams = meta.getTagParams(tableName);
                parameterValues.addAll(tagParams);
            }

            sb.append(" ").append(meta.getInsertSql()).append(" values ");
            entitySqls.forEach(item -> {
                sb.append(meta.getParamSql()).append(" ");
                parameterValues.addAll(item.getValues());
            });
        });

        return new SqlExecContext(sb, parameterValues.stream().toArray(SqlParameterValue[]::new), execEntitySqls.size(), remain);
    }

    /**
     * 单条执行
     * @param entity
     * @return
     */
    public SqlExecContext getExecContext(Object entity) {
        final String tableName = this.getTableName(entity);
        StringBuilder sb = new StringBuilder(Insert).append(tableName).append(" ");

        List<SqlParameterValue> parameterValues = new ArrayList<>();
        if(this.getMeta().getsTable().using()) { // 数据表不存在自动创建
            if(!CollectionUtils.isEmpty(this.getMeta().getTags())) { // 使用tags创建
                sb.append(meta.getTagInsertSql()).append(" tags ").append(meta.getTagParamSql());
                parameterValues.addAll(meta.getTagParams(tableName));
            } else {
                sb.append(meta.getTagInsertSql());
            }
        }

        sb.append(" ").append(meta.getInsertSql()).append(" values ").append(meta.getParamSql());
        parameterValues.addAll(this.meta.getParams(entity));

        // 默认执行1条
        return new SqlExecContext(sb, parameterValues.stream().toArray(SqlParameterValue[]::new), 1, 0);
    }

    public String getTableName(Object entity) {
        // 获取数据表名
        String tableName = getMeta().getsTable().table();

        // 解析数据表表名
        return parser.parseExpression(tableName).getValue(entity, String.class);
    }

    @Override
    public void run() {
        try {
            int maxOfPeer = this.getTaosHandle().maxOfPeer();
            do {
                final int size = this.entitySqls.size();
                if(size <= 0) return;

                final int truncateIndex = maxOfPeer > size ? size : maxOfPeer;
                List<EntitySql> entitySqls = this.entitySqls.subList(0, truncateIndex);

                SqlExecContext  execContext = this.getExecContext(entitySqls, size - truncateIndex);
                this.doUpdate(execContext, this.jdbcTemplate);

                synchronized (this) {
                    // 移除已经入库的记录
                    this.entitySqls.truncate(truncateIndex);
                }

            } while (this.entitySqls.size() > maxOfPeer);
        } catch (TaosException e) {

        } catch (Exception e) {
            logger.error("写入Sql失败", e);
        }
    }

    /**
     * 单条数据入库
     * @param entity
     * @return
     */
    public int update(Object entity) {
        return this.doUpdate(this.getExecContext(entity), this.jdbcTemplate);
    }

    public int update(Object entity, JdbcTemplate jdbcTemplate) {
        return this.doUpdate(this.getExecContext(entity), jdbcTemplate);
    }

    protected int doUpdate(SqlExecContext context, JdbcTemplate jdbcTemplate) throws TaosException {
        int i;
        try {
            i = jdbcTemplate.update(context.getSql().toString(), context.getValues());
            writeLogger(context, null);
        } catch (DataAccessException e) {
            this.writeLogger(context, e);
            throw new TaosException(e);
        }

        return i;
    }

    private void writeLogger(SqlExecContext execContext, Throwable cause) {
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (SqlParameterValue value : execContext.getValues()) {
                sb.append(value.getValue()).append(", ");
            }

            logger.trace("TAOS适配 数据入库 - 入库/剩余(条)：{}/{} - \r\n\t    Sql：{} \r\n\t params：{}"
                    , execContext.getCount(), execContext.getRemain(), execContext.getSql().toString(), sb.substring(0, sb.length() - 2), cause);
        }
    }

    public EntityMetas getMeta() {
        return meta;
    }

    public void setMeta(EntityMetas meta) {
        this.meta = meta;
    }

    public TruncateLinkedList<EntitySql> getEntitySqls() {
        return entitySqls;
    }

    public TaosHandle getTaosHandle() {
        return taosHandle;
    }

    public void setTaosHandle(TaosHandle taosHandle) {
        this.taosHandle = taosHandle;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
