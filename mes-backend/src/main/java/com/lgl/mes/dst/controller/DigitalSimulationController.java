package com.lgl.mes.dst.controller;



import com.lgl.mes.common.BaseController;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 数字仿真控制器 基于three.js
 * </p>
 *
 * @author lgl
 * @since 2020-08-18
 */
@Controller
@RequestMapping("/digital/simulation")
public class DigitalSimulationController extends BaseController{
    /**
     * 数字仿真界面
     *
     * @return 数字仿真界面
     */
    @ApiOperation("数字仿真3D教学UI")
    @GetMapping("/list-ui")
    public String addOrUpdateUI() {
        return "digitization/3DProject";
    }

}
