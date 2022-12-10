package com.dream.iot.test.db.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author dreamer，75039960@qq.com
 * @date 2021/8/27
 */
@Component
public class SpMetaObjectHandler implements MetaObjectHandler {

    Logger logger = LoggerFactory.getLogger(SpMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("start insert fill ...");
        this.setInsertData(metaObject);
        this.setUpdateData(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("start update fill ...");
        this.setUpdateData(metaObject);
    }

    private void setInsertData(MetaObject metaObject) {
        //SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        this.setInsertFieldValByName("createUsername", "lgl", metaObject);
        this.setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
    }

    private void setUpdateData(MetaObject metaObject) {
        //SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        this.setUpdateFieldValByName("updateUsername", "lgl", metaObject);
        this.setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
