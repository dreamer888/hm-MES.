package com.dream.iot.test.db.order.request;

import com.dream.iot.test.db.common.BasePageReq;

/**
 * 物料分页对象*
 * @author dreamer,75039960@qq.com
 * @since 2022/06/01
 */
public class spOrderReq extends BasePageReq {

    /**
     *模糊查询  order
     */
    private String orderCode;

    private String status;

    //private  LocalDateTime  createTime1;
    private  String  createTime1;
    private  String  createTime2;


    public String getOrderCode() {
        return this.orderCode;
    }
    public void setOrderCode(String orderCode)  { this.orderCode= orderCode;}

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status)  {
        this.status= status;
    }

    public String getCreateTime1() {
        return this.createTime1;
    }
    public String getCreateTime2() {
        return this.createTime2;
    }

    public void setCreateTime1(String createTime1) {
        this.createTime1 = createTime1;
    }
    public void setCreateTime2(String createTime2) {
        this.createTime2 = createTime2;
    }

}
