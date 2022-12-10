package com.lgl.mes.system.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.common.BaseController;
import com.lgl.mes.common.Result;
import com.lgl.mes.system.entity.SysDepartment;
import com.lgl.mes.system.entity.SysDict;
import com.lgl.mes.system.request.SysDepartmentPageReq;
import com.lgl.mes.system.service.ISysDepartmentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 系统部门前端控制器
 * </p>
 *
 * @author dreamer，75039960@qq.com
 * @since 2020-03-03
 */
@Controller
@RequestMapping("/admin/sys/department")
public class SysDepartmentController extends BaseController {

    Logger logger = LoggerFactory.getLogger(SysDepartmentController.class);

    @Autowired
    private ISysDepartmentService sysDepartmentService;

    @ApiOperation("系统部门信息列表UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {
        return "admin/system/department/list";
    }

    @ApiOperation("系统部门信息分页列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "模型", defaultValue = "模型")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(SysDepartmentPageReq req) {
        IPage result = sysDepartmentService.page(req);
        return Result.success(result);
    }

    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SysDict record) {
        if (StringUtils.isNotEmpty(record.getId())) {
            SysDepartment sysDepartment = sysDepartmentService.getById(record.getId());
            model.addAttribute("department", sysDepartment);
        }
        return "admin/system/department/addOrUpdate";
    }

    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(SysDepartment record) {
        sysDepartmentService.saveOrUpdate(record);
        return Result.success(record.getId());
    }
}
