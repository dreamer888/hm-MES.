package com.lgl.mes.basedata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgl.mes.basedata.entity.SpTableManager;
import com.lgl.mes.basedata.entity.SpTableManagerItem;

import java.util.List;

/**
 * <p>
 * 主数据表头服务类
 * </p>
 *
 * @author lgl
 * @since 2020-03-06
 */
public interface ISpTableManagerService extends IService<SpTableManager> {
    /**
     * 查询表对应的字段
     *
     * @param req 表信息
     * @return 字段信息
     */
    List<SpTableManagerItem> queryTableFieldByName(SpTableManager req) throws Exception;

}
