package com.dream.iot.test.db.order.entity;

import com.dream.iot.test.db.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-12
 */
@ApiModel(value="SpOrder对象", description="")
public class SpOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单编号")
    private String orderCode;

    @ApiModelProperty(value = "工单描述")
    private String orderDescription;

    @ApiModelProperty(value = "计划产品数量")
    private Integer planQty;

    @ApiModelProperty(value = "订单类型 P 量产 A验证 F返工 ")
    private String orderType;

    @ApiModelProperty(value = "工艺流程")
    private String flow;

    @ApiModelProperty(value = "产品类型")
    private String product;

    @ApiModelProperty(value = "计划开始时间")
    private String planStartTime;

    @ApiModelProperty(value = "计划结束时间")
    private String planEndTime;

    @ApiModelProperty(value = "1,创建 2 进行中，3订单结束，4订单终结")
    private String status;

    @ApiModelProperty(value = "生产线")
    private String line;

    @ApiModelProperty(value = "已经生产数量")
    private Integer makedQty;

    @ApiModelProperty(value = "不合格数量")
    private Integer badQty;

    @ApiModelProperty(value = "质检通过率")
    private Float passRate;

    @ApiModelProperty(value = "订单二维码,可以从二维码解析出多个字段，依赖于客户的具体需求，保留字段")
    private String qrcode;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "订单完成率")
    private Float finishRate;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }
    public Integer getPlanQty() {
        return planQty;
    }

    public void setPlanQty(Integer planQty) {
        this.planQty = planQty;
    }
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }
    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
    public Integer getMakedQty() {
        return makedQty;
    }

    public void setMakedQty(Integer makedQty) {
        this.makedQty = makedQty;
    }
    public Integer getBadQty() {
        return badQty;
    }

    public void setBadQty(Integer badQty) {
        this.badQty = badQty;
    }
    public Float getPassRate() {
        return passRate;
    }

    public void setPassRate(Float passRate) {
        this.passRate = passRate;
    }
    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Float getFinishRate() {
        return finishRate;
    }

    public void setFinishRate(Float finishRate) {
        this.finishRate = finishRate;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpOrder{" +
            "orderCode=" + orderCode +
            ", orderDescription=" + orderDescription +
            ", planQty=" + planQty +
            ", orderType=" + orderType +
            ", flow=" + flow +
            ", product=" + product +
            ", planStartTime=" + planStartTime +
            ", planEndTime=" + planEndTime +
            ", status=" + status +
            ", globalId=" + line +
            ", makedQty=" + makedQty +
            ", badQty=" + badQty +
            ", passRate=" + passRate +
            ", qrcode=" + qrcode +
            ", memo=" + memo +
            ", finishRate=" + finishRate +
        "}";
    }
}
