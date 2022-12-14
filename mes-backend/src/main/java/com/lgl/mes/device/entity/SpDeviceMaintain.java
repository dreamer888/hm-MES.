package com.lgl.mes.device.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-08-17
 */
@TableName("sp_device_maintain")
@ApiModel(value="SpDeviceMaintain对象", description="设备维护信息表")
public class SpDeviceMaintain extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称")
    private String device;

    @ApiModelProperty(value = "故障描述")
    private String errorDesc;

    @ApiModelProperty(value = "处理者")
    private String dealer;

    @ApiModelProperty(value = "处理结果")
    private String result;

    @ApiModelProperty(value = "逻辑删除：1 表示删除，0 表示未删除，2 表示禁用")
    @TableField(value = "is_deleted")
    private String deleted;


    @ApiModelProperty(value = "0正常运行，1故障，2维修中 3 未启用")
    private String flag;


    @ApiModelProperty(value = "故障时长")
    private Integer badTime;

    public Integer getBadTime() {
        return badTime;
    }

    public void setBadTime(Integer badTime) {
        this.badTime = badTime;
    }



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
    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
        return "SpDeviceMaintain{" +
            "device=" + device +
            ", errorDesc=" + errorDesc +
            ", dealer=" + dealer +
            ", result=" + result +
                ", flag=" + flag +
                ", badTime=" + badTime +
            ", deleted=" + deleted +
        "}";
    }
}
