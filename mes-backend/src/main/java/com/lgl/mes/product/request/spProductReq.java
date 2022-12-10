package com.lgl.mes.product.request;

import com.lgl.mes.common.BasePageReq;

import java.time.LocalDateTime;

/**
 * 分页对象
 * @author lgl  75039960@qq.com
 * @since 2022/04/01
 */
public class spProductReq extends BasePageReq {

    /**
     *模糊查询product
     */
    private String productIdLike;

    private String bomCodeLike;

    private String qrcodeLike;


    //private  LocalDateTime  createTime1;
    private  String  createTime1;
    private  String  createTime2;

    //createTime1

    public String getProductIdLike() {
        return this.productIdLike;
    }
    public String getQrcodeLike() {
        return this.qrcodeLike;
    }
    public String getBomCodeLike() {
        return this.bomCodeLike;
    }
    public String getCreateTime1() {
        return this.createTime1;
    }
    public String getCreateTime2() {
        return this.createTime2;
    }

    public void setProductIdLike(String productIdLike)  { this.productIdLike= productIdLike;}

    public void setBomCodeLike(String bomCodeLike) {
        this.bomCodeLike = bomCodeLike;
    }

    public void setQrcodeLike(String qrcodeLike) {
        this.qrcodeLike = qrcodeLike;
    }
    public void setCreateTime1(String createTime1) {
        this.createTime1 = createTime1;
    }
    public void setCreateTime2(String createTime2) {
        this.createTime2 = createTime2;
    }

}
