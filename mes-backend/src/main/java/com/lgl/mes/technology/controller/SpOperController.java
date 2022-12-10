package com.lgl.mes.technology.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.basedata.request.SpTableManagerReq;
import com.lgl.mes.common.Result;
import com.lgl.mes.product.entity.SpProduct;
import com.lgl.mes.technology.dto.SpFlowDto;
import com.lgl.mes.technology.entity.SpFlow;
import com.lgl.mes.technology.entity.SpOper;
import com.lgl.mes.technology.service.ISpFlowOperRelationService;
import com.lgl.mes.technology.service.ISpOperService;
import com.lgl.mes.technology.vo.SpOperVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import com.lgl.mes.common.BaseController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lgl
 * @since 2020-03-14
 */
@Controller
@RequestMapping("/basedata/operation")
public class SpOperController extends BaseController {
    @Autowired
    public ISpOperService iSpOperService;
    /**
     * 流程服务
     */

    /**
     * 流程管理界面
     *
     * @param model 模型
     * @return 流程管理界面
     */
    @ApiOperation("流程UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {
        return "technology/operation/list";
    }


    /**
     * 流程编辑界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("流程管理编辑界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpOper record) throws Exception {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpOper spOper = iSpOperService.getById(record.getId());
            model.addAttribute("result", spOper);
        }
        return "technology/operation/addOrUpdate";
    }


    /**
     * 流程信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("流程信息分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(SpTableManagerReq req) {
           IPage result = iSpOperService.page(req);
        return Result.success(result);
    }


    /**
     * 流程与工序关系管理新增+修改
     *
     * @param spOper 流程与工序DTO
     * @return 执行结果
     */
    @ApiOperation("流程新增+修改")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(/*@RequestBody*/ SpOper spOper) throws Exception {
        iSpOperService.saveOrUpdate(spOper);

        return Result.success();
    }

    /**
     * 删除流程
     *
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("delete流程")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "流程实体", defaultValue = "流程实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpOper req) throws Exception {

            iSpOperService.removeById(req.getId());

        return Result.success();
    }



}
