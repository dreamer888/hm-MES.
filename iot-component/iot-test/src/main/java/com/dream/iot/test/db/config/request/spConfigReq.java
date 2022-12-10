package com.dream.iot.test.db.config.request;

import com.dream.iot.test.db.common.BasePageReq;

/**
 * 分页对象
 * @author lgl  75039960@qq.com
 * @since 2022/08/01
 */
public class spConfigReq extends BasePageReq {

    /**
     *模糊查询product
     */
    private String lineIdLike;


    public String getLineIdLike() {
        return this.lineIdLike;
    }
    public void setLineIdLike(String lineIdLike)  { this.lineIdLike= lineIdLike;}


}
