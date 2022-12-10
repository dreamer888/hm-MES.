package com.dream.iot.test.db.globalId.entity;

import com.dream.iot.test.db.common.BaseEntity;
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


    @ApiModelProperty(value = "生产线")
    private String line;


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }



    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpGlobalId{" +
                "orderCode=" + orderCode +
                "line=" + line +

                "}";
    }
}
