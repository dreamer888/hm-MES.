package com.lgl.mes.technology.request;

import com.lgl.mes.common.BasePageReq;

/**
 * bom 分页请求
 *
 * @author lgl
 * @since 20200328
 */

public class SpBomReq extends BasePageReq {
    /**
     * 物料模糊查询
     */
    private  String materielCodeLike ;
    private  String bomCodeLike ;
    /**
     * 获取 物料模糊查询
     *
     * @return materielCodeLike 物料模糊查询
     */
    public String getMaterielCodeLike() {
        return this.materielCodeLike;
    }

    /**
     *
     * @return bomCodeLike
     */
    public String getBomCodeLike() {
        return this.bomCodeLike;
    }

    /**
     * 设置 物料模糊查询
     *
     * @param materielCodeLike 物料模糊查询
     */
    public void setMaterielCodeLike(String materielCodeLike) {
        this.materielCodeLike = materielCodeLike;
    }

    /**
     * bom模糊查询
     * @param bomCodeLike
     */
    public void setBomlCodeLike(String bomCodeLike) {
        this.materielCodeLike = bomCodeLike;
    }
}
