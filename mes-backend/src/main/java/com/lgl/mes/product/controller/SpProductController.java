package com.lgl.mes.product.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.daily.entity.SpDailyPlan;
import com.lgl.mes.daily.service.ISpDailyPlanService;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.service.ISpOrderService;
import com.lgl.mes.product.entity.SpProduct;
import com.lgl.mes.basedata.entity.SpTableManager;

import com.lgl.mes.product.request.spProductReq;
import com.lgl.mes.common.Result;

import com.lgl.mes.product.service.ISpProductService;
import com.lgl.mes.technology.entity.SpFlow;
import com.lgl.mes.technology.service.ISpFlowService;
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
import java.util.List;

import  com.lgl.mes.product.request.spProductReqWrapper;
import  com.lgl.mes.common.util.DateUtil;

import org.springframework.beans.BeanUtils;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lgl
 * @since 2022-06-27
 */
@Controller
@RequestMapping("/product")
public class SpProductController extends BaseController {

    @Autowired
    private ISpProductService iSpProductService;
    @Autowired
    private ISpFlowService iSpFlowService;

    @Autowired
    private ISpOrderService iSpOrderService;
    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;

    @ApiOperation("产品列表界面UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {

        return "product/list";
    }


    /**
     * 物料管理修改界面
     *
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("产品管理修改界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpTableManager record) {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpProduct spProduct = iSpProductService.getById(record.getId());
            model.addAttribute("result", spProduct);
        }
        return "product/addOrUpdate";
    }



    /**
     * 产品管理界面分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("产品管理界面分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(spProductReq req) {
        QueryWrapper<SpProduct> queryWrapper =new QueryWrapper();
        //QueryWrapper queryWrapper =new QueryWrapper();

       /* if (StringUtils.isNotEmpty(req.getProductIdLike()))
        {
            queryWrapper.like("productId",req.getProductIdLike());
        }*/

        if (StringUtils.isNotEmpty(req.getBomCodeLike()))
        {
            queryWrapper.like("bomCode",req.getBomCodeLike());
        }

        if (StringUtils.isNotEmpty(req.getQrcodeLike()))
        {
            queryWrapper.like("qrcode",req.getQrcodeLike());
        }

        if (req.getCreateTime1() !=null && !req.getCreateTime1().isEmpty())
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate= LocalDate.parse(req.getCreateTime1(), formatter);
            //queryWrapper.ge("createTime",ld);
            queryWrapper.apply("UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + localDate + "')");
        }


        if (req.getCreateTime2() !=null && !req.getCreateTime2().isEmpty())
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate= LocalDate.parse(req.getCreateTime2(), formatter);
            queryWrapper.apply("UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + localDate + "')");
        }

        IPage result = iSpProductService.page(req,queryWrapper);
        return Result.success(result);
    }



    @ApiOperation("产品管理修改、新增")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(SpProduct record) {

        if(record==null || record.getProductId()==null ||record.getProductId().isEmpty())
        {
            return Result.failure("没有订单数据！");
        }

        if(record.getId()!=null &&  !record.getId().isEmpty()) {
            SpFlow spflow = iSpFlowService.getById(record.getFlowId());
            if (spflow != null)
                record.setFlowDesc(spflow.getFlowDesc());

            iSpProductService.saveOrUpdate(record);

            return Result.success();

        }
        else  //新增产品
        {
            if (iSpProductService.AddProdut(record))
                return  Result.success();
            else return  Result.failure();
        }
    }


    /**
     * 删除产品信息
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("删除产品信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "产品实体", defaultValue = "产品实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpProduct req) throws Exception {
        iSpProductService.removeById(req.getId());
        return Result.success();
    }




    @ApiOperation("产品不良率管控UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/badProdutUi")
    public String badProdutUi(Model model) {

        return "product/badProduct";
    }

    @ApiOperation("产品不良率管控")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "产品不良率实体", defaultValue = "产品不良率实体")})
    @PostMapping("/getBadProduct")
    @ResponseBody
    public Result getBadProduct(SpProduct req) throws Exception {
        //iSpProductService.removeById(req.getId());
        return Result.success();
    }


    @ApiOperation("产品不良率管控")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "产品不良率实体", defaultValue = "产品不良率实体")})
    @PostMapping("/updateBadProduct")
    @ResponseBody
    public Result updateBadProduct(SpProduct req) throws Exception {
        //iSpProductService.removeById(req.getId());
        return Result.success();
    }


}
