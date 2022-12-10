package com.lgl.mes.product.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lgl.mes.common.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgl
 * @since 2022-06-27
 */
@ApiModel(value="SpProduct对象", description="")
public class SpProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "产品id")
    private String productId;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "bom code")
    private String bomCode;

    @ApiModelProperty(value = "产品二维码")
    private String qrcode;

    @ApiModelProperty(value = "产品批次")
    private String batchNo;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "产品品质: 0 合格，1 不合格 ")
    private String quality;

    @ApiModelProperty(value = "产品库位： 0， 车间，1 成品仓库，2 出库")
    private Integer position;


    private String flowId;
    /**
     * 流程描述
     */
    private String flowDesc;

    @ApiModelProperty(value = "不良品工位")
    private Integer badPos;




    @TableField(value = "is_deleted")
    @ApiModelProperty(value = "逻辑删除：1 表示删除，0 表示未删除，2 表示禁用")
    private String deleted;


    public Integer getBadPos() {
        return badPos;
    }

    public void setBadPos(Integer badPos) {
        this.badPos = badPos;
    }



    /* @ApiModelProperty(value = "逻辑删除：1 表示删除，0 表示未删除，2 表示禁用")
    private String isDeleted;
*/
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }
    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getFlowId() {
        return this.flowId;
    }

    /**
     * 设置 流程
     *
     * @param flowId 流程
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
     * 获取 流程描述
     *
     * @return flowDesc 流程描述
     */
    public String getFlowDesc() {
        return this.flowDesc;
    }

    /**
     * 设置 流程描述
     *
     * @param flowDesc 流程描述
     */
    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpProduct{" +
                "productId=" + productId +
                ", name=" + name +
                ", bomCode=" + bomCode +
                ", qrcode=" + qrcode +
                ", batchNo=" + batchNo +
                ", orderNo=" + orderNo +
                ", quality=" + quality +
                ", position=" + position +
                ", deleted=" + deleted +
                ", flowId=" + flowId +
                ", badPos=" + badPos +
                ", flowDesc=" + flowDesc +

                "}";
    }
}
