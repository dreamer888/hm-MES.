package com.lgl.mes.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 基础分页查询参数
 *
 * @author dreamer，75039960@qq.com
 * @date 2021/9/27 16:05
 */
public class BasePageReq extends Page {

    private String orderBy = "update_time";

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
