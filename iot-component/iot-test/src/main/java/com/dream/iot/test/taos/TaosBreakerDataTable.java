package com.dream.iot.test.taos;

import com.dream.iot.taos.STable;
import com.dream.iot.tools.annotation.IotField;

import java.util.Date;

/**
 * 测试直接使用数据表
 */
@STable(table = "'t_'+#root.sn")
public class TaosBreakerDataTable {

    @IotField
    private Date ts;

    private String sn;
    @IotField
    private double v; // 电压
    @IotField
    private double i; // 电流
    @IotField
    private double power1; // 有功功率
    @IotField
    private double power2; // 无功功率
    @IotField
    private double py; // 功率因素

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getPower1() {
        return power1;
    }

    public void setPower1(double power1) {
        this.power1 = power1;
    }

    public double getPower2() {
        return power2;
    }

    public void setPower2(double power2) {
        this.power2 = power2;
    }

    public double getPy() {
        return py;
    }

    public void setPy(double py) {
        this.py = py;
    }
}
