package com.lgl.mes.daily.entity;

//import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.lgl.mes.common.BaseEntity;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-17
 */


@Data
@ApiModel(value="SpDailyPlan对象", description="")
public class SpDailyPlan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "计划日期")
    private String planDate;

    @ApiModelProperty(value = "每件产品耗时 ，以秒为单位 (这个是设定的标准)")
    private Integer pieceTime;

    @ApiModelProperty(value = "实际每件耗时(平均每秒耗时,不是最近一件产品的耗时，也不是最近一段时间内的制造一件产品的耗时)")
    private Integer realPieceTime;

    @ApiModelProperty(value = "计划产量")
    private Integer planQty;

    @ApiModelProperty(value = "当前产量")
    private Integer makedQty;

    @ApiModelProperty(value = "次品数量")
    private Integer badQty;

    @ApiModelProperty(value = "当日完成率 ")
    private Float finishRate;

    @ApiModelProperty(value = "一次通过率")
    private Float passRate;

    @ApiModelProperty(value = "每个小时计划生产产品数")
    private Integer hourQty;

    @ApiModelProperty(value = "每分钟计划生产产品数")
    private Integer minuteQty;

    @ApiModelProperty(value = "逻辑删除：1 表示删除，0 表示未删除，2 表示禁用")
    private Integer isDeleted;

    @ApiModelProperty(value = "下午开始时间 默认  下午一点")
    private String afternoonStart;

    @ApiModelProperty(value = "下午结束时间")
    private String afternoonEnd;

    @ApiModelProperty(value = "夜班开始时间")
    private String eveningStart;

    @ApiModelProperty(value = "夜班结束时间")
    private String eveningEnd;

    @ApiModelProperty(value = "早班开始时间")
    private String morningStart;

    @ApiModelProperty(value = "早班结束时间")
    private String morningEnd;

    @ApiModelProperty(value = "最近一件产品生产时间")
    private LocalDateTime lastTime;

    @ApiModelProperty(value = "理论完成率 =当日实际消耗时间/当日排产有效时间,")
    private float expectFinishRate;

    @ApiModelProperty(value = "下午1开始时间 默认  下午一点")
    private String afternoonStart1;

    @ApiModelProperty(value = "下午1结束时间")
    private String afternoonEnd1;

    @ApiModelProperty(value = "夜班开始时间")
    private String eveningStart1;

    @ApiModelProperty(value = "夜班结束时间")
    private String eveningEnd1;

    @ApiModelProperty(value = "早班开始时间")
    private String morningStart1;

    @ApiModelProperty(value = "早班结束时间")
    private String morningEnd1;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }
    public Integer getPieceTime() {
        return pieceTime;
    }

    public void setPieceTime(Integer pieceTime) {
        this.pieceTime = pieceTime;
    }
    public Integer getRealPieceTime() {
        return realPieceTime;
    }

    public void setRealPieceTime(Integer realPieceTime) {
        this.realPieceTime = realPieceTime;
    }
    public Integer getPlanQty() {
        return planQty;
    }

    public void setPlanQty(Integer planQty) {
        this.planQty = planQty;
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
    public Float getFinishRate() {
        return finishRate;
    }

    public void setFinishRate(Float finishRate) {
        this.finishRate = finishRate;
    }
    public Float getPassRate() {
        return passRate;
    }

    public void setPassRate(Float passRate) {
        this.passRate = passRate;
    }
    public Integer getHourQty() {
        return hourQty;
    }

    public void setHourQty(Integer hourQty) {
        this.hourQty = hourQty;
    }
    public Integer getMinuteQty() {
        return minuteQty;
    }

    public void setMinuteQty(Integer minuteQty) {
        this.minuteQty = minuteQty;
    }
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    public String getAfternoonStart() {
        return afternoonStart;
    }

    public void setAfternoonStart(String afternoonStart) {
        this.afternoonStart = afternoonStart;
    }
    public String getAfternoonEnd() {
        return afternoonEnd;
    }

    public void setAfternoonEnd(String afternoonEnd) {
        this.afternoonEnd = afternoonEnd;
    }
    public String getEveningStart() {
        return eveningStart;
    }

    public void setEveningStart(String eveningStart) {
        this.eveningStart = eveningStart;
    }
    public String getEveningEnd() {
        return eveningEnd;
    }

    public void setEveningEnd(String eveningEnd) {
        this.eveningEnd = eveningEnd;
    }
    public String getMorningStart() {
        return morningStart;
    }

    public void setMorningStart(String morningStart) {
        this.morningStart = morningStart;
    }
    public String getMorningEnd() {
        return morningEnd;
    }

    public void setMorningEnd(String morningEnd) {
        this.morningEnd = morningEnd;
    }
    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }
    public float getExpectFinishRate() {
        return expectFinishRate;
    }

    public void setExpectFinishRate(float expectFinishRate) {
        this.expectFinishRate = expectFinishRate;
    }
    public String getAfternoonStart1() {
        return afternoonStart1;
    }

    public void setAfternoonStart1(String afternoonStart1) {
        this.afternoonStart1 = afternoonStart1;
    }
    public String getAfternoonEnd1() {
        return afternoonEnd1;
    }

    public void setAfternoonEnd1(String afternoonEnd1) {
        this.afternoonEnd1 = afternoonEnd1;
    }
    public String getEveningStart1() {
        return eveningStart1;
    }

    public void setEveningStart1(String eveningStart1) {
        this.eveningStart1 = eveningStart1;
    }
    public String getEveningEnd1() {
        return eveningEnd1;
    }

    public void setEveningEnd1(String eveningEnd1) {
        this.eveningEnd1 = eveningEnd1;
    }
    public String getMorningStart1() {
        return morningStart1;
    }

    public void setMorningStart1(String morningStart1) {
        this.morningStart1 = morningStart1;
    }
    public String getMorningEnd1() {
        return morningEnd1;
    }

    public void setMorningEnd1(String morningEnd1) {
        this.morningEnd1 = morningEnd1;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpDailyPlan{" +
                "orderCode=" + orderCode +
                ", planDate=" + planDate +
                ", pieceTime=" + pieceTime +
                ", realPieceTime=" + realPieceTime +
                ", planQty=" + planQty +
                ", makedQty=" + makedQty +
                ", badQty=" + badQty +
                ", finishRate=" + finishRate +
                ", passRate=" + passRate +
                ", hourQty=" + hourQty +
                ", minuteQty=" + minuteQty +
                ", isDeleted=" + isDeleted +
                ", afternoonStart=" + afternoonStart +
                ", afternoonEnd=" + afternoonEnd +
                ", eveningStart=" + eveningStart +
                ", eveningEnd=" + eveningEnd +
                ", morningStart=" + morningStart +
                ", morningEnd=" + morningEnd +
                ", lastTime=" + lastTime +
                ", expectFinishRate=" + expectFinishRate +
                ", afternoonStart1=" + afternoonStart1 +
                ", afternoonEnd1=" + afternoonEnd1 +
                ", eveningStart1=" + eveningStart1 +
                ", eveningEnd1=" + eveningEnd1 +
                ", morningStart1=" + morningStart1 +
                ", morningEnd1=" + morningEnd1 +
                "}";
    }
}
