package com.lgl.mes.device.request;

import com.lgl.mes.common.BasePageReq;

/**
 * 分页对象
 * @author lgl  75039960@qq.com
 * @since 2022/08/01
 */
public class spDeviceReq extends BasePageReq {

    /**
     *模糊查询device
     */
    private String deviceLike;

    public String getDeviceLike() {
        return this.deviceLike;
    }

    public void setDeviceLike(String deviceLike)  { this.deviceLike= deviceLike;}

}
