package com.lgl.mes.daily.service;

import com.lgl.mes.daily.entity.SpDailyPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-02
 */
public interface ISpDailyPlanService extends IService<SpDailyPlan> {


    public  SpDailyPlan  getTodayPanByOrder(String  orderCode);
    public float GenFinishRate(SpDailyPlan spDailyPlan);

    public  Boolean  FlushFinishRate(String  orderCode);


}
