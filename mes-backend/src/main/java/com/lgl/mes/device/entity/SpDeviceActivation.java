package com.lgl.mes.device.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lgl.mes.common.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 工序表
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-08-18
 */
@TableName("sp_device_activation")
@ApiModel(value="SpDeviceActivation对象", description="工序表")
public class SpDeviceActivation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称")
    private String device;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "逻辑删除：1 表示删除，0 表示未删除，2 表示禁用")
    private String isDeleted;

    @ApiModelProperty(value = "计划负载时长(已经扣除中间正常休息时间)")
    private Integer planTime;

    @ApiModelProperty(value = "实际负载时长=plan_time-bad_time-wait_time")
    private Integer workTime;

    @ApiModelProperty(value = "故障时长(统计当日的,从维修信息表中统计)")
    private Integer badTime;

    @ApiModelProperty(value = "设备稼动率=work_time/plan_time  %100")
    private Float activationRate;

    @ApiModelProperty(value = "等待时长(统计当日的,这个 不好统计)")
    private Integer waitTime;

    @ApiModelProperty(value = "设备状态: 0正常运行，1故障，2维修中 3 未启用")
    private String flag;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
    public Integer getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Integer planTime) {
        this.planTime = planTime;
    }
    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }
    public Integer getBadTime() {
        return badTime;
    }

    public void setBadTime(Integer badTime) {
        this.badTime = badTime;
    }
    public Float getActivationRate() {
        return activationRate;
    }

    public void setActivationRate(Float activationRate) {
        this.activationRate = activationRate;
    }
    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpDeviceActivation{" +
            "device=" + device +
            ", supplier=" + supplier +
            ", isDeleted=" + isDeleted +
            ", planTime=" + planTime +
            ", workTime=" + workTime +
            ", badTime=" + badTime +
            ", activationRate=" + activationRate +
            ", waitTime=" + waitTime +
            ", flag=" + flag +
        "}";
    }
}
