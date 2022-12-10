package com.dream.iot.test.db.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dream.iot.test.db.config.entity.SpConfig;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-08-23
 */
public interface ISpConfigService extends IService<SpConfig> {

    public  SpConfig GetCurrentConfig( String lineId);

}
