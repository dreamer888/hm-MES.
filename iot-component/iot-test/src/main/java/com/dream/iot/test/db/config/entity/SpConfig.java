package com.dream.iot.test.db.config.entity;

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
 * @since 2022-10-04
 */
@ApiModel(value="SpConfig对象", description="")
public class SpConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "生产线id")
    private String lineId;

    @ApiModelProperty(value = "服务器ip")
    private String serverIp;

    @ApiModelProperty(value = "服务器port")
    private Integer serverPort;

    @ApiModelProperty(value = "本地port")
    private Integer localPort;

    @ApiModelProperty(value = "更新数据的时间间隔 秒,interval是保留字不能使用")
    private Integer interv;

    @ApiModelProperty(value = "通讯协议类型 0 tcp   client1 tcp  server2 udp  server3 udp  client4 fins  client5 simens client")
    private String comType;

    @ApiModelProperty(value = "是否使用串口0 不适用， 1 使用")
    private Integer useComm;

    @ApiModelProperty(value = "波特率")
    private Integer bandrate;

    @ApiModelProperty(value = "数据位")
    private Integer dataBits;

    @ApiModelProperty(value = "停止位")
    private Integer stopBits;

    @ApiModelProperty(value = "0 奇校验，1偶校验")
    private Integer checkBit;

    @ApiModelProperty(value = "front  url")
    private String fronturl;

    @ApiModelProperty(value = "rabbitmq url")
    private String rabbitmqurl;

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }
    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }
    public Integer getInterv() {
        return interv;
    }

    public void setInterv(Integer interv) {
        this.interv = interv;
    }
    public String getComType() {
        return comType;
    }

    public void setComType(String comType) {
        this.comType = comType;
    }
    public Integer getUseComm() {
        return useComm;
    }

    public void setUseComm(Integer useComm) {
        this.useComm = useComm;
    }
    public Integer getBandrate() {
        return bandrate;
    }

    public void setBandrate(Integer bandrate) {
        this.bandrate = bandrate;
    }
    public Integer getDataBits() {
        return dataBits;
    }

    public void setDataBits(Integer dataBits) {
        this.dataBits = dataBits;
    }
    public Integer getStopBits() {
        return stopBits;
    }

    public void setStopBits(Integer stopBits) {
        this.stopBits = stopBits;
    }
    public Integer getCheckBit() {
        return checkBit;
    }

    public void setCheckBit(Integer checkBit) {
        this.checkBit = checkBit;
    }
    public String getFronturl() {
        return fronturl;
    }

    public void setFronturl(String fronturl) {
        this.fronturl = fronturl;
    }
    public String getRabbitmqurl() {
        return rabbitmqurl;
    }

    public void setRabbitmqurl(String rabbitmqurl) {
        this.rabbitmqurl = rabbitmqurl;
    }

    //@Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SpConfig{" +
            "lineId=" + lineId +
            ", serverIp=" + serverIp +
            ", serverPort=" + serverPort +
            ", localPort=" + localPort +
            ", interv=" + interv +
            ", comType=" + comType +
            ", useComm=" + useComm +
            ", bandrate=" + bandrate +
            ", dataBits=" + dataBits +
            ", stopBits=" + stopBits +
            ", checkBit=" + checkBit +
            ", fronturl=" + fronturl +
            ", rabbitmqurl=" + rabbitmqurl +
        "}";
    }
}
