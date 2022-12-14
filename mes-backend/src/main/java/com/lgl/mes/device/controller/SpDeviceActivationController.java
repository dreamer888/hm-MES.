package com.lgl.mes.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.common.Result;
import com.lgl.mes.device.entity.SpDeviceActivation;
import com.lgl.mes.device.request.spDeviceActivationReq;
import com.lgl.mes.device.service.ISpDeviceActivationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import com.lgl.mes.common.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 工序表 前端控制器
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-08-18
 */
@Controller
@RequestMapping("/device/activation")
public class SpDeviceActivationController extends BaseController {
    @Autowired
    public ISpDeviceActivationService iSpDeviceActivationService;

    /**
     * 管理界面
     *
     * @param model 模型
     * @return 管理界面
     */
    @ApiOperation("设备维护UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {
        return "device/activation/list";
    }


    /**
     * 流程编辑界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("设备维护管理编辑界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpDeviceActivation record) throws Exception {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpDeviceActivation spDeviceActivation = iSpDeviceActivationService.getById(record.getId());
            model.addAttribute("result", spDeviceActivation);
        }
        return "device/activation/addOrUpdate";
    }


    /**
     * 流程信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("device维护分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(spDeviceActivationReq req) {

        QueryWrapper<SpDeviceActivation> queryWrapper =new QueryWrapper();

        if (StringUtils.isNotEmpty(req.getDeviceLike()))
        {
            queryWrapper.like("device",req.getDeviceLike());
        }

        if (req.getCreateTime1() !=null && !req.getCreateTime1().isEmpty())
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate= LocalDate.parse(req.getCreateTime1(), formatter);
            queryWrapper.apply("UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + localDate + "')");
        }

        if (req.getCreateTime2() !=null && !req.getCreateTime2().isEmpty())
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate= LocalDate.parse(req.getCreateTime2(), formatter);
            queryWrapper.apply("UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + localDate + "')");
        }

        IPage result = iSpDeviceActivationService.page(req,queryWrapper);
        return Result.success(result);

    }


    /**
     * 流程与工序关系管理新增+修改
     *
     * @param spDeviceActivation 流程与工序DTO
     * @return 执行结果
     */
    @ApiOperation("device维护新增+修改")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(/*@RequestBody*/ SpDeviceActivation spDeviceActivation) throws Exception {
        iSpDeviceActivationService.saveOrUpdate(spDeviceActivation);

        return Result.success();
    }

    /**
     * 删除流程
     *
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("delete设备维护记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "device实体", defaultValue = "device实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpDeviceActivation req) throws Exception {

        iSpDeviceActivationService.removeById(req.getId());

        return Result.success();
    }

}
