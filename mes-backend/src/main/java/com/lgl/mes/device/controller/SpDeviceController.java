package com.lgl.mes.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.basedata.request.SpTableManagerReq;
import com.lgl.mes.common.Result;
import com.lgl.mes.device.entity.SpDevice;
import com.lgl.mes.device.entity.SpDeviceMaintain;
import com.lgl.mes.device.request.spDeviceReq;
import com.lgl.mes.device.service.ISpDeviceService;
import com.lgl.mes.technology.entity.SpOper;
import com.lgl.mes.technology.service.ISpOperService;
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
 * 前端控制器
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-08-17
 */
@Controller
@RequestMapping("/device/device")
public class SpDeviceController extends BaseController {
    @Autowired
    public ISpDeviceService iSpDeviceService;


    /**
     * 设备管理界面
     *
     * @param model 模型
     * @return 设备管理界面
     */
    @ApiOperation("设备UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {
        return "device/device/list";
    }


    /**
     *device编辑界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("设备管理编辑界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpDevice record) throws Exception {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpDevice spDevice = iSpDeviceService.getById(record.getId());
            model.addAttribute("result", spDevice);
        }
        return "device/device/addOrUpdate";
    }


    /**
     * 流程信息分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("device信息分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(spDeviceReq req) {
        QueryWrapper<SpDevice> queryWrapper =new QueryWrapper();

        if (StringUtils.isNotEmpty(req.getDeviceLike()))
        {
            queryWrapper.like("device",req.getDeviceLike());
        }

        IPage result = iSpDeviceService.page(req,queryWrapper);
        return Result.success(result);
    }


    /**
     * 流程与工序关系管理新增+修改
     *
     * @param spDevice 流程与工序DTO
     * @return 执行结果
     */
    @ApiOperation("device新增+修改")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(/*@RequestBody*/ SpDevice spDevice) throws Exception {
        iSpDeviceService.saveOrUpdate(spDevice);

        return Result.success();
    }

    /**
     * 删除流程
     *
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("delete设备记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "device实体", defaultValue = "device实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpDevice req) throws Exception {

        iSpDeviceService.removeById(req.getId());

        return Result.success();
    }


}
