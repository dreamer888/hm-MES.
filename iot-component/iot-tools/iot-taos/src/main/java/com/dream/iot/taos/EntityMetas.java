package com.dream.iot.taos;

import com.dream.iot.tools.annotation.IotField;
import com.dream.iot.tools.annotation.IotFieldMeta;
import com.dream.iot.tools.annotation.IotTagMeta;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntityMetas {

    private STable sTable;

    private String insertSql;

    private String paramSql;

    private Class<?> entityClass;

    private String tagInsertSql;

    private String tagParamSql;

    private TagsResolver tagsResolver;

    private List<IotTagMeta> tags = new ArrayList<>();
    private List<IotFieldMeta> fields = new ArrayList<>();

    private static SqlParameterValue NULL = new SqlParameterValue(Types.NULL, null);

    public EntityMetas(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityMetas resolve(BeanFactory factory) {
        this.sTable = entityClass.getAnnotation(STable.class);
        if(this.sTable == null) return null;

        if(this.sTable.tags().length > 0) {
            for (String tagName : this.sTable.tags()) {
                tags.add(new IotTagMeta(tagName, null, null));
            }
        }

        // 使用超级表
        if(this.sTable.using() && !CollectionUtils.isEmpty(this.tags)) {
            if(this.sTable.tagsResolver() == null) {
                throw new TaosException("["+entityClass.getSimpleName()+"]请在STable里指定TagsResolver");
            } else {
                this.tagsResolver = factory.getBean(this.sTable.tagsResolver(), TagsResolver.class);
            }
        }

        Field[] declaredFields = entityClass.getDeclaredFields();
        for(Field item : declaredFields) {
            if(!item.isAccessible()) {
                item.setAccessible(true);
            }

            IotField field = item.getAnnotation(IotField.class);
            if(field != null) {
                IotFieldMeta meta = new IotFieldMeta(field, Objects.equals(field.value(), "") ? item.getName() : field.value(), item);
                fields.add(meta);
            }
        }

        this.paramSql = "("+this.fields.stream().map(item -> "?").collect(Collectors.joining(","))+")";
        this.insertSql = "(" + this.fields.stream().map(item -> item.getName()).collect(Collectors.joining(","))+")";

        if(this.sTable.using()) {
            if(!CollectionUtils.isEmpty(this.tags)) {
                if (StringUtils.hasText(sTable.value())) {
                    this.tagInsertSql = "USING " + sTable.value() + " (" + tags.stream().map(item -> item.getName()).collect(Collectors.joining(",")) + ")";
                    this.tagParamSql = "(" + this.tags.stream().map(item -> "?").collect(Collectors.joining(",")) + ")";
                } else {
                    throw new TaosException("["+entityClass.getSimpleName()+"]自动创建数据表必须指定超级表表名[STable#value()]");
                }
            } else {
                throw new TaosException("["+entityClass.getSimpleName()+"]使用超级表创建子表必须指定对应的Tags[STable#tags()]");
            }
        }

        return this;
    }

    public List<SqlParameterValue> getParams(Object entity) {
        List<SqlParameterValue> parameterValue = new ArrayList<>();

        // 再解析fields
        for (int i=0; i<this.fields.size(); i++) {
            SqlParameterValue value = getValue(entity, this.fields.get(i).getField());
            parameterValue.add(value);
        }

        return parameterValue;
    }

    public List<SqlParameterValue> getTagParams(String tableName) {
        List<SqlParameterValue> params = new ArrayList<>();
        if(!CollectionUtils.isEmpty(this.tags)) {
            this.tags.forEach(item -> {
                TagValue tagValue = this.tagsResolver.resolve(tableName, item.getName());
                if(tagValue != null) {
                    params.add(new SqlParameterValue(tagValue.getType(), tagValue.getValue()));
                } else {
                    params.add(NULL);
                }
            });
        }

        return params;
    }

    private SqlParameterValue getValue(Object entity, Field field) {
        Object value = ReflectionUtils.getField(field, entity);
        int sqlType = Types.NULL;
        if(value != null) {
            if(value instanceof Number) {
                if(value instanceof Integer) {
                    sqlType = Types.INTEGER;
                } else if(value instanceof Long) {
                    sqlType = Types.BIGINT;
                } else if(value instanceof Double) {
                    sqlType = Types.DOUBLE;
                } else if(value instanceof Float) {
                    sqlType = Types.FLOAT;
                } else if(value instanceof Short) {
                    sqlType = Types.SMALLINT;
                } else if(value instanceof Byte) {
                    sqlType = Types.TINYINT;
                }
            } else if(value instanceof String) {
                sqlType = Types.NCHAR;
            } else if(value instanceof Boolean) {
                sqlType = Types.BOOLEAN;
            } else if(value instanceof Date) {
                sqlType = Types.TIMESTAMP;
            }
        }

        return new SqlParameterValue(sqlType, value);
    }

    public String getInsertSql() {
        return insertSql;
    }

    public String getParamSql() {
        return paramSql;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public STable getsTable() {
        return sTable;
    }

    public TagsResolver getTagsResolver() {
        return tagsResolver;
    }

    public List<IotTagMeta> getTags() {
        return tags;
    }

    public List<IotFieldMeta> getFields() {
        return fields;
    }

    public String getTagInsertSql() {
        return tagInsertSql;
    }

    public void setTagInsertSql(String tagInsertSql) {
        this.tagInsertSql = tagInsertSql;
    }

    public String getTagParamSql() {
        return tagParamSql;
    }

    public void setTagParamSql(String tagParamSql) {
        this.tagParamSql = tagParamSql;
    }
}
