package com.dream.iot.taos;

import com.dream.iot.Protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来声明超级表名, 可以自动创建数据表
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface STable {

    /**
     * 超级表表名
     * @return 返回超级表表名, 如果没有超级表请使用空字符串：""
     */
    String value() default "";

    /**
     * 数据表表名 使用SpEL表达式
     * class TestEntity {
     *     private String sn;
     *     get set 省略
     * }
     * 数据表名=前缀D + 设备编号, 那么SpEl表达式 = "'D'+#root.sn"
     * @see TaosHandle#handle(Protocol) 返回值 = #root
     * @return
     */
    String table();

    /**
     * 用来解析tags值, 解析器的名称必须可以通过spring容器获取得到
     * e.g: INSERT INTO d21001 USING meters TAGS ('Beijing.Chaoyang', 2)
     * @see TagsResolver
     * @return
     */
    String tagsResolver() default "";

    /**
     * 是否自动创建数据表
     * @return
     */
    boolean using() default false;

    /**
     * 创建子表需要使用的Tags
     * @see #using() true
     * @see #value() 超级表表名
     * @see #tagsResolver() tags值解析
     * @return
     */
    String[] tags() default {};
}
