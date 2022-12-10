package com.lgl.mes.daily.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.common.util.DateUtil;
import com.lgl.mes.daily.entity.SpDailyPlan;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.service.ISpOrderService;
import com.lgl.mes.product.entity.SpProduct;
import com.lgl.mes.basedata.entity.SpTableManager;

import com.lgl.mes.daily.request.spDailyPlanReq;
import com.lgl.mes.product.request.spProductReq;
import com.lgl.mes.common.Result;
import com.lgl.mes.common.util.DateUtil;

import com.lgl.mes.daily.service.ISpDailyPlanService;

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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dreamer,75039960@qq.com
 * @since 2022-06-27
 */
@Controller
@RequestMapping("/daily/plan")
public class SpDailyPlanController extends BaseController {

    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;

    @Autowired
    private ISpOrderService iSpOrderService;

    @ApiOperation("每日计划列表界面UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {

        return "daily/list";
    }


    /**
     * 每日计划管理修改界面
     *
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("每日计划管理修改界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpTableManager record) {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpDailyPlan spDailyPlan = iSpDailyPlanService.getById(record.getId());
            model.addAttribute("result", spDailyPlan);
        }
        return "daily/addOrUpdate";
    }

    /**
     * 每日计划管理界面分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("每日计划管理界面分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page( spDailyPlanReq  req) {   //  //spDailyPlanReq
        QueryWrapper<SpDailyPlan> queryWrapper =new QueryWrapper();
        if (StringUtils.isNotEmpty(req.getOrderCode()))
        {
            queryWrapper.like("order_code",req.getOrderCode());
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

        IPage result = iSpDailyPlanService.page(req,queryWrapper);



        return Result.success(result);
    }



    @ApiOperation("每日计划管理修改、新增")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(SpDailyPlan   record)  throws Exception {
        if(record.getPlanDate()!=null ) {
            //record.setPlanDate(DateUtil.GetShortStampString(record.getPlanDate().toString()));
            record.setPlanDate(DateUtil.GetStampString(record.getPlanDate().toString()));
        }

        if(record.getMorningStart()!=null )
            record.setMorningStart( DateUtil.GetStampString(record.getMorningStart().toString()));
        if(record.getMorningEnd()!=null )
            record.setMorningEnd( DateUtil.GetStampString(record.getMorningEnd().toString()));
        if(record.getMorningStart1()!=null )
            record.setMorningStart1( DateUtil.GetStampString(record.getMorningStart1().toString()));
        if(record.getMorningEnd1()!=null )
            record.setMorningEnd1( DateUtil.GetStampString(record.getMorningEnd1().toString()));



        if(record.getAfternoonStart()!=null )
            record.setAfternoonStart( DateUtil.GetStampString(record.getAfternoonStart().toString()));
        if(record.getAfternoonEnd()!=null )
            record.setAfternoonEnd( DateUtil.GetStampString(record.getAfternoonEnd().toString()));
        if(record.getAfternoonStart1()!=null )
            record.setAfternoonStart1( DateUtil.GetStampString(record.getAfternoonStart1().toString()));
        if(record.getAfternoonEnd1()!=null )
            record.setAfternoonEnd1( DateUtil.GetStampString(record.getAfternoonEnd1().toString()));

        if(record.getEveningStart()!=null )
            record.setEveningStart( DateUtil.GetStampString(record.getEveningStart().toString()));
        if(record.getEveningEnd()!=null )
            record.setEveningEnd( DateUtil.GetStampString(record.getEveningEnd().toString()));
        if(record.getEveningStart1()!=null )
            record.setEveningStart1( DateUtil.GetStampString(record.getEveningStart1().toString()));
        if(record.getEveningEnd1()!=null )
            record.setEveningEnd1( DateUtil.GetStampString(record.getEveningEnd1().toString()));


        iSpDailyPlanService.saveOrUpdate(record);

        return Result.success();
    }


    /**
     * 删除计划信息
     * 删除当日计划信息,如果是删除订单的最后一条信息，则删除订单计划
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("删除当日计划信息,如果是删除订单的最后一条信息，则删除订单计划")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "产品实体", defaultValue = "产品实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByTableNameId(SpDailyPlan req) throws Exception {

         if(req.getId()==null)
         {
             return  Result.failure("没有订单ID");
         }
        SpDailyPlan spDailyPlan =iSpDailyPlanService.getById(req.getId());
         String orderCode = spDailyPlan.getOrderCode();
        iSpDailyPlanService.removeById(req.getId());

        /////////////////////如果是删除订单的最后一条信息，则删除订单计划////
        QueryWrapper<SpDailyPlan> queryWrapperDaily = new QueryWrapper();
        queryWrapperDaily.eq("order_code", orderCode);
        List<SpDailyPlan> list =iSpDailyPlanService.list(queryWrapperDaily);
        if(list ==null  || list.size()==0){
            QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_code", orderCode);
            SpOrder spOrder = iSpOrderService.getOne(queryWrapper);
            iSpOrderService.removeById(spOrder.getId());
        }

        return Result.success();
    }

}
