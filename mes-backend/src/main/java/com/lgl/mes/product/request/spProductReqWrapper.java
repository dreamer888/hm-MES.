package com.lgl.mes.product.request;

import com.lgl.mes.common.BasePageReq;
import   com.lgl.mes.product.entity.SpProduct;

/**
 * 分页对象
 * @author lgl  75039960@qq.com
 * @since 2022/04/01
 */
public class spProductReqWrapper extends SpProduct {



    //private  LocalDateTime  createTime;
    private  String  createTimeWrapper;


    public String getCreateTimeWrapper() {
        return this.createTimeWrapper;
    }
    public void setCreateTimeWrapper(String createTimeWrapper) {
        this.createTimeWrapper = createTimeWrapper;
    }


}
