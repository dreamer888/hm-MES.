package com.lgl.mes.digitization.request;

import com.lgl.mes.common.BasePageReq;
import io.swagger.annotations.ApiModelProperty;
import com.lgl.mes.daily.entity.SpDailyPlan;
import com.lgl.mes.order.entity.SpOrder;

/**
 * 物料分页对象*
 * @author dreamer,75039960@qq.com
 * @since 2022/06/01
 */
public class spDataReq extends BasePageReq {

    public SpOrder spOrder;
    public SpDailyPlan  spDailyPlan;

}
