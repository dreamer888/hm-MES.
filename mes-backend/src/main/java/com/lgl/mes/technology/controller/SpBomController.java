package com.lgl.mes.technology.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.common.BaseController;
import com.lgl.mes.common.Result;
import com.lgl.mes.technology.entity.SpBom;
import com.lgl.mes.technology.request.SpBomReq;
import com.lgl.mes.technology.service.ISpBomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * BOM前端控制器
 * </p>
 *
 * @author lgl
 * @since 2020-03-28
 */
@Controller
@RequestMapping("/technology/bom")
public class SpBomController extends BaseController {
    /**
     * bom服务
     */
    @Autowired
    private ISpBomService iSpBomService;

    /**
     * 工艺BOM管理界面
     *
     * @param model 模型
     * @return 工艺BOM管理界面
     */
    @ApiOperation("工艺BOM管理界面UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {

        return "technology/bom/list";
    }


    /**
     * 工艺BOM管理修改界面
     *
     * @param model 模型
     * @param spBom bom实体
     * @return 更改界面
     */
    @ApiOperation("工艺BOM管理修改界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpBom spBom) throws Exception {
        if (StringUtils.isNotEmpty(spBom.getId())) {
            SpBom result = iSpBomService.getById(spBom.getId());
            model.addAttribute("result", result);
        }

        return "technology/bom/addOrUpdate";
    }


    /**
     * 工艺BOM分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("工艺BOM分页分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(SpBomReq req) {
        QueryWrapper qw = new QueryWrapper();
        if (StringUtils.isNotEmpty(req.getMaterielCodeLike())) {
            qw.likeRight("materiel_code",req.getMaterielCodeLike());
        }

        /*  AND  */
        if (StringUtils.isNotEmpty(req.getBomCodeLike())) {
            qw.likeRight("bom_code",req.getBomCodeLike());
        }

        IPage result = iSpBomService.page(req,qw);
        return Result.success(result);
    }

    /**
     * 工艺BOM修改、新增
     *
     * @param spBom 物料实体类
     * @return 执行结果
     */
    @ApiOperation("工艺BOM修改、新增")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(SpBom spBom) {
        iSpBomService.saveOrUpdate(spBom);
        return Result.success();
    }


    /**
     * 删除工艺BOM
     *
     * @param spBom 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("删除工艺BOM")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "工艺BOM", defaultValue = "工艺BOM")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpBom spBom) throws Exception {
        iSpBomService.removeById(spBom.getId());
        return Result.success();
    }

}
