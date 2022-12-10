package com.dream.iot.test.db.daily.request;

import com.dream.iot.test.db.common.BasePageReq;

/**
 * 分页对象
 * @author lgl
 * @since 2022/04/01
 */
public class spDailyPlanReq extends BasePageReq {

    /**
     *模糊查询product
     */
    private String orderCode;

    //private  LocalDateTime  createTime1;
    private  String  createTime1;
    private  String  createTime2;

    //createTime1

    public String getOrderCode() {
        return this.orderCode;
    }

    public String getCreateTime1() {
        return this.createTime1;
    }
    public String getCreateTime2() {
        return this.createTime2;
    }

    public void setOrderCode(String orderCode)  { this.orderCode= orderCode;}
    public void setCreateTime1(String createTime1) {
        this.createTime1 = createTime1;
    }
    public void setCreateTime2(String createTime2) {
        this.createTime2 = createTime2;
    }

}
