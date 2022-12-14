package com.lgl.mes.device.request;

import com.lgl.mes.common.BasePageReq;

/**
 * 分页对象
 * @author lgl  75039960@qq.com
 * @since 2022/04/01
 */
public class spDeviceMaintainReq extends BasePageReq {

    /**
     *模糊查询
     */
    private String deviceLike;

    private  String  createTime1;
    private  String  createTime2;

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

    public String getDeviceLike() {
        return this.deviceLike;
    }
    public void setDeviceLike(String deviceLike)  { this.deviceLike= deviceLike;}


}
