package com.lgl.mes.technology.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgl.mes.technology.dto.SpFlowDto;
import com.lgl.mes.technology.entity.SpFlowOperRelation;
import com.lgl.mes.technology.vo.SpOperVo;
import com.lgl.mes.common.Result;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lgl
 * @since 2020-03-14
 */
public interface ISpFlowOperRelationService extends IService<SpFlowOperRelation> {
    /**
     * 流程与工序关系新增与修改
     *
     * @param spFlowDto 流程信息DTO
     * @return 执行结果
     * @throws Exception 异常
     */
    Result addOrUpdate(SpFlowDto spFlowDto) throws Exception;

    /**
     * 全部工序集合
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<SpOperVo> allOperViewServer() throws Exception;

    /**
     * 绘制当前流程下的工序
     *
     * @return 工序VO集合
     * @throws Exception 异常
     */
    List<SpOperVo> currentOperViewServer(String flowId) throws Exception;
}
