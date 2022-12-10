package com.lgl.mes.technology.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.common.BaseController;
import com.lgl.mes.common.Result;
import com.lgl.mes.technology.entity.SpFlow;
import com.lgl.mes.technology.request.SpFlowReq;
import com.lgl.mes.technology.service.ISpFlowService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 流程控制器
 * </p>
 *
 * @author lgl
 * @since 2020-03-14
 */
@Controller
@RequestMapping("/basedata/flow")
public class SpFlowController extends BaseController {

    @Autowired
    public ISpFlowService iSpFlowService;

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
    public Result page(SpFlowReq req) {
        IPage result = iSpFlowService.page(req);
        return Result.success(result);
    }


    /**
     * 流程全部信息查询
     *
     * @return Result 执行结果
     */
    @ApiOperation("流程全部信息查询")
    @GetMapping("/list")
    @ResponseBody
    public Result list() {
        QueryWrapper queryWrapper = new QueryWrapper();
        //queryWrapper.eq("is_deleted", "0");
        List<SpFlow> list = iSpFlowService.list(queryWrapper);
        return Result.success(list);
    }

}
