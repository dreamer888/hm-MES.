package com.lgl.mes.basedata.request;

import com.lgl.mes.common.BasePageReq;
/**
 * 物料分页对象
 * @author lgl
 * @since 2022/04/01
 */
public class spMaterileReq  extends BasePageReq {
    /**
     *模糊查询物料编号
     */
    private String materielLike;
    /**
     *模糊查询物料描述
     */
    private String materielDescLike;

    private String batchNoLike;

    private String qrcodeLike;

    /**
     * 获取 模糊查询物料编号
     *
     * @return materielLike 模糊查询物料编号
     */
    public String getMaterielLike() {
        return this.materielLike;
    }
    public String getBatchNoLike() {
        return this.batchNoLike;
    }
    public String getQrcodeLike() {
        return this.qrcodeLike;
    }
    /**
     * 设置 模糊查询物料编号
     *
     * @param materielLike 模糊查询物料编号
     */
    public void setMaterielLike(String materielLike) {
        this.materielLike = materielLike;
    }

    public void setBatchNoLike(String batchNoLike) {
        this.batchNoLike = batchNoLike;
    }

    public void setQrcodeLike(String qrcodeLike) {
        this.qrcodeLike = qrcodeLike;
    }



    /**
     * 获取 模糊查询物料描述
     *
     * @return materielDescLike 模糊查询物料描述
     */
    public String getMaterielDescLike() {
        return this.materielDescLike;
    }

    /**
     * 设置 模糊查询物料描述
     *
     * @param materielDescLike 模糊查询物料描述
     */
    public void setMaterielDescLike(String materielDescLike) {
        this.materielDescLike = materielDescLike;
    }
}
