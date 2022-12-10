package com.lgl.mes.globalId.entity;

import com.lgl.mes.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 线体表
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-01
 */
@ApiModel(value="全局订单编号", description="当前订单")
public class SpGlobalId extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前订单号")
    private String orderCode;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpGlobalId{" +
            "orderCode=" + orderCode +

        "}";
    }
}
