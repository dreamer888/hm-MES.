package com.lgl.mes.basedata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.basedata.entity.SpMaterile;
import com.lgl.mes.basedata.entity.SpTableManager;
import com.lgl.mes.basedata.request.spMaterileReq;
import com.lgl.mes.basedata.service.ISpMaterileService;
import com.lgl.mes.common.BaseController;
import com.lgl.mes.common.Result;
import com.lgl.mes.technology.entity.SpFlow;
import com.lgl.mes.technology.service.ISpFlowService;
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
 * 物料控制器
 * </p>
 *
 * @author lgl
 * @since 2020-03-19
 */
@Controller
@RequestMapping("/basedata/materile")
public class SpMaterileController extends BaseController {

    /**
     * 物料服务
     * @date 2020-07-07
     */
    @Autowired
    private ISpMaterileService iSpMaterileService;
    /**
     * 流程服务
     */
    @Autowired
    private ISpFlowService iSpFlowService;

    /**
     * 物料管理界面
     *
     * @param model 模型
     * @return 物料管理界面
     */
    @ApiOperation("物料管理界面UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {
        return "basedata/materile/list";
    }


    /**
     * 物料管理修改界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("物料管理修改界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpTableManager record) {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpMaterile SpMaterile = iSpMaterileService.getById(record.getId());
            model.addAttribute("result", SpMaterile);
        }
        return "basedata/materile/addOrUpdate";
    }


    /**
     * 物料管理界面分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("物料管理界面分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(spMaterileReq req) {
        QueryWrapper queryWrapper =new QueryWrapper();
        if (StringUtils.isNotEmpty(req.getMaterielLike()))
        {
            queryWrapper.like("materiel",req.getMaterielLike());
        }
        if (StringUtils.isNotEmpty(req.getMaterielDescLike()))
        {
            queryWrapper.like("materiel_desc",req.getMaterielDescLike());
        }

        if (StringUtils.isNotEmpty(req.getBatchNoLike()))
        {
            queryWrapper.like("batch_no",req.getBatchNoLike());
        }


        if (StringUtils.isNotEmpty(req.getQrcodeLike()))
        {
            queryWrapper.like("qrcode",req.getQrcodeLike());
        }



        IPage result = iSpMaterileService.page(req,queryWrapper);
        return Result.success(result);
    }

    /**
     * 物料管理修改、新增
     *
     * @param record 物料实体类
     * @return 执行结果
     */
    @ApiOperation("物料管理修改、新增")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(SpMaterile record) {
        SpFlow spflow = iSpFlowService.getById(record.getFlowId());
        if (spflow!=null)
            record.setFlowDesc(spflow.getFlowDesc());
        iSpMaterileService.saveOrUpdate(record);
        return Result.success();
    }


    /**
     * 删除物料信息
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("删除物料信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "物料实体", defaultValue = "物料实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpMaterile req) throws Exception {
        iSpMaterileService.removeById(req.getId());
        return Result.success();
    }
}
