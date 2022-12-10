package com.lgl.mes.device.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-08-17
 */
@ApiModel(value="SpDevice对象", description="设备表")
public class SpDevice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称")
    private String device;

    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "逻辑删除：1 表示删除，0 表示未删除，2 表示禁用")
    @TableField(value = "is_deleted")
    private String deleted;

    @ApiModelProperty(value = "0正常运行，1故障，2维修中 3 未启用")
    private String flag;


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpDevice{" +
            "device=" + device +
            ", deviceDesc=" + deviceDesc +
            ", supplier=" + supplier +
                ", flag=" + flag +
            ", deleted=" + deleted +
        "}";
    }
}
