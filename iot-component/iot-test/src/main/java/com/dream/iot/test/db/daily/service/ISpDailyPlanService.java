package com.dream.iot.test.db.daily.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dream.iot.test.db.daily.entity.SpDailyPlan;
import com.dream.iot.test.db.order.entity.SpOrder;

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
    public  SpDailyPlan  getTodayPanByOrder(SpOrder order);
    public float GenFinishRate(SpDailyPlan spDailyPlan);

    public  Boolean  FlushFinishRate(String  orderCode);


}
