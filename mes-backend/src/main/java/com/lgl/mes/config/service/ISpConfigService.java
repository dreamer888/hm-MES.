package com.lgl.mes.config.service;

import com.lgl.mes.config.entity.SpConfig;
import com.baomidou.mybatisplus.extension.service.IService;

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
