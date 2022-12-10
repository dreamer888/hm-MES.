package com.lgl.mes.digitization.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lgl.mes.common.BaseController;
import com.lgl.mes.daily.service.ISpDailyPlanService;
import com.lgl.mes.globalId.entity.SpGlobalId;
import com.lgl.mes.globalId.service.ISpGlobalIdService;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.service.ISpOrderService;
import com.lgl.mes.product.service.ISpProductService;
//import com.lgl.mes.daily.service.ISpDailyPlanService;

import com.lgl.mes.test.entity.SpTest;
import com.lgl.mes.test.service.ISpTestService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import  com.lgl.mes.common.Result;
import com.lgl.mes.daily.entity.SpDailyPlan;
import  com.lgl.mes.digitization.request.spDataReq;
import  com.lgl.mes.digitization.request.spOrderReq;
import com.lgl.mes.product.entity.SpProduct;


import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import  com.alibaba.fastjson.JSON.*;
import  com.alibaba.fastjson.JSONObject;

/**
 * 系统登录
 *
 * @author dreamer，75039960@qq.com
 * @date 2021/9/27 16:05
 */
@RequestMapping("/digitization/plan")
@Controller("PlanDataController")
public class PlanDataController extends BaseController {

    Logger logger = LoggerFactory.getLogger(PlanDataController.class);
    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;
    @Autowired
    private ISpOrderService iSpOrderService;
    //private ISpDailyPlanService iSpDailyPlanService;



    @Autowired
    private ISpProductService iSpProductService;

    @Autowired
    private ISpTestService iSpTestService;

    @Autowired
    private ISpGlobalIdService iSpGlobalIdService;



    /**
     * 工单计划数字化看板
     *
     * @param model 视图对象
     * @return 工单计划数字化看板界面
     */
    @ApiOperation("工单计划数字化看板")
    @GetMapping("/plan-ui")
    public String welcomeUI(Model model) {
        return "digitization/planDemo";
    }

    @ApiOperation("工单计划数字化看板")
    @GetMapping("/plan-ui2")
    public String bigScreen2(Model model) {
        return "redirect:http://127.0.0.1///index.html#";
        // return "digitization/70/index";
    }


    @ApiOperation("工单计划数字化看板")
    @GetMapping("/plan-ui1")
    public String bigScreen() {

        return "redirect:http://www.dreammm.net";
        //return "digitization/planDemo";
    }


    @GetMapping("/getActualProcess")
    @ResponseBody
    public Result getActualProcess() {

        DateTime dt= new DateTime();
        String sdt = dt.toString("yyyy-MM-dd hh:mm:ss");
        Number n = (dt.getSeconds()%60) ;

        return Result.success(n,"当前进度"); //uccessData(200, n).put("msg", "当前进度");
    }

    @PostMapping("/loadOrder")
    @ResponseBody
    public Result loadOrder(@RequestBody SpOrder  para) {
       String orderCode = para.getOrderCode();//jsonObj.getString("orderCode");

        QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(orderCode)) {
            queryWrapper.eq("order_code", orderCode);
        }
        else {

            //Result<spDataReq>  res = new Result<spDataReq>();

            return Result.failure("订单号码不能为空");
        }

        try{
            SpOrder spOrder = iSpOrderService.getOne(queryWrapper);
            //WeakReference<SpOrder> abcWeakRef = new WeakReference<SpOrder>(iSpOrderService.getOne(queryWrapper));
            QueryWrapper<SpDailyPlan> queryWrapperDaily = new QueryWrapper();
            queryWrapperDaily.eq("order_code", orderCode);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);
            queryWrapperDaily.like("plan_date", dateString);

            SpDailyPlan spDailyPlan = iSpDailyPlanService.getOne(queryWrapperDaily);

                if(spOrder==null || spDailyPlan==null) {
                return Result.failure("没有查到数据，请检查输入--每个订单在当日只能排产一次,只能加载排期在当日订单");
                //return Result.failure("请检查输入");//限制8 个字 ？？？
              }

          /*  不能在这里刷新， 计算量太大
            SpDailyPlan  sdp = new SpDailyPlan();
            BeanUtils.copyProperties(spDailyPlan, sdp);

            float expectFihinsRate = iSpDailyPlanService.GenFinishRate(sdp);
            spDailyPlan.setExpectFinishRate(expectFihinsRate);
        */
            spDataReq dataReq=new spDataReq();
            dataReq.spOrder = spOrder;
            dataReq.spDailyPlan = spDailyPlan;

            if( para.getOrderDescription()!=null && para.getOrderDescription().equals(orderCode))
            {
                QueryWrapper<SpGlobalId> queryWrapper2 = new QueryWrapper();
                queryWrapper.like("order_code", "");
                List<SpGlobalId> list = iSpGlobalIdService.list(queryWrapper2);;

                SpGlobalId spGlobalId = iSpGlobalIdService.getById(list.get(0).getId()); //order_code其实也是唯一的
                spGlobalId.setOrderCode(orderCode);

                iSpGlobalIdService.saveOrUpdate(spGlobalId);
            }
            return Result.success(dataReq,"成功返回订单计划数据");

        }
        catch (Exception e) {
         Result.failure("加载数据错误 ，"+e.getMessage());
        }

        return Result.success(null,"成功返回订单计划数据"); //uccessData(200, n).put("msg", "当前进度");
    }



    @PostMapping("/loadOrderOnce")
    @ResponseBody
    public Result loadOrderOnce(@RequestBody SpOrder  para) {
        String orderCode = para.getOrderCode();//jsonObj.getString("orderCode");

        QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(orderCode)) {
            queryWrapper.eq("order_code", orderCode);
        }
        else {

            //Result<spDataReq>  res = new Result<spDataReq>();

            return Result.failure("订单号码不能为空");
        }

        try{
            SpOrder spOrder = iSpOrderService.getOne(queryWrapper);
            //WeakReference<SpOrder> abcWeakRef = new WeakReference<SpOrder>(iSpOrderService.getOne(queryWrapper));
            QueryWrapper<SpDailyPlan> queryWrapperDaily = new QueryWrapper();
            queryWrapperDaily.eq("order_code", orderCode);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);
            queryWrapperDaily.like("plan_date", dateString);

            SpDailyPlan spDailyPlan = iSpDailyPlanService.getOne(queryWrapperDaily);

            if(spOrder==null || spDailyPlan==null) {
                return Result.failure("没有查到数据，请检查输入--每个订单在当日只能排产一次,只能加载排期在当日订单");
                //return Result.failure("请检查输入");//限制8 个字 ？？？
            }


            spDataReq dataReq=new spDataReq();
            dataReq.spOrder = spOrder;
            dataReq.spDailyPlan = spDailyPlan;

            if( para.getOrderDescription()!=null && para.getOrderDescription().equals(orderCode))
            {
                QueryWrapper<SpGlobalId> queryWrapper2 = new QueryWrapper();
                queryWrapper.like("order_code", "");
                List<SpGlobalId> list = iSpGlobalIdService.list(queryWrapper2);;

                SpGlobalId spGlobalId = iSpGlobalIdService.getById(list.get(0).getId()); //order_code其实也是唯一的
                spGlobalId.setOrderCode(orderCode);

                iSpGlobalIdService.saveOrUpdate(spGlobalId);
            }
            return Result.success(dataReq,"成功返回订单计划数据");

        }
        catch (Exception e) {
            Result.failure("加载数据错误 ，"+e.getMessage());
        }

        return Result.success(null,"成功返回订单计划数据"); //uccessData(200, n).put("msg", "当前进度");
    }





    /**
     * 订单计划列表  按时间顺序取 前 30 条 ,limit 30
     * @param para
     * @return
     */
    @PostMapping("/orderPlanList")
    @ResponseBody
    public Result orderList(@RequestBody spOrderReq  para) {

        QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
            try {

                queryWrapper.orderByDesc("create_time");
                queryWrapper.last("limit 30");
                List<SpOrder> list = iSpOrderService.list(queryWrapper);
                return Result.success(list,"返回订单计划数据"+list.size()+"条");
            }
            catch (Exception e) {
                Result.failure("查询失败 ，"+e.getMessage());
            }

        return Result.success(null,"返回订单计划数据"); //uccessData(200, n).put("msg", "当前进度");
    }



    /**
     * 该订单的产品列表  按时间顺序取 ,limit 30
     * @param para
     * @return
     */
    @PostMapping("/productList")
    @ResponseBody
    public Result productList(@RequestBody spOrderReq  para) {
        String orderCode = para.getOrderCode();//jsonObj.

        QueryWrapper<SpProduct> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(orderCode)) {
            try {
                //queryWrapper.eq("order_no", orderCode);
                queryWrapper.orderByDesc("create_time");
                queryWrapper.last("limit 30");
                List<SpProduct> list = iSpProductService.list(queryWrapper);
                return Result.success(list,"返回产品数据 "+list.size()+"条");
            }
            catch (Exception e) {
                Result.failure("查询失败 ，"+e.getMessage());
            }
        }
        else {
            return Result.failure("订单号码不能为空");
        }

        return Result.success(null,"成功返回订单计划数据"); //uccessData(200, n).put("msg", "当前进度");
    }


    @PostMapping("/recentBadProductList")
    @ResponseBody
    public Result recentBadProductList() {
        QueryWrapper<SpDailyPlan> queryWrapper = new QueryWrapper();
        try {
            queryWrapper.orderByDesc("create_time");
            queryWrapper.last("limit 6");
            List<SpDailyPlan> list = iSpDailyPlanService.list(queryWrapper);


            List<Integer> listInt =  new ArrayList<Integer>();

            //需要保证6个数据
            for(int i=0; i<6;i++)
            {
                listInt.add(0);
            }
            for(int i=0; i<list.size();i++)
            {
                listInt.set(6-i-1,list.get(i).getBadQty());
            }
            return Result.success(listInt,"返回不每日良品数据"+listInt.size()+"条");
        }
        catch (Exception e) {
            Result.failure("查询失败 ，"+e.getMessage());
        }

        return Result.success(null,"返回产量数据"); //uccessData(200, n).put("msg", "当前进度");

    }


    @PostMapping("/recentProductAvgDiffList")
    @ResponseBody
    public Result recentProductAvgDiffList() {
        QueryWrapper<SpDailyPlan> queryWrapper = new QueryWrapper();
        try {
            queryWrapper.orderByDesc("create_time");
            queryWrapper.last("limit 6");
            List<SpDailyPlan> list = iSpDailyPlanService.list(queryWrapper);


            List<Integer> listInt =  new ArrayList<Integer>();

            Long total= Long.valueOf(0);
            Long avg= Long.valueOf(0);

            for(int i=0; i<list.size();i++)
            {
                total= total+ list.get(i).getMakedQty();
            }
            if (list.size()==0) avg=Long.valueOf(0);
            else avg=  total/list.size()  ;

            //需要保证6个数据,
            for(int i=0; i<6;i++)
            {
                listInt.add(0);
            }
            for(int i=0; i<list.size();i++)
            {
                listInt.set(6-i-1, (int) (list.get(i).getMakedQty()-avg));
            }
            return Result.success(listInt,"返回近期产量均差数据"+listInt.size()+"条");
        }
        catch (Exception e) {
            Result.failure("查询失败 ，"+e.getMessage());
        }

        return Result.success(null,"返回近期产量均差数据"); //uccessData(200, n).put("msg", "当前进度");

    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @PostMapping("/FlushFinishRate")
    @ResponseBody
    public Result FlushFinishRate(@RequestBody spOrderReq  para) {
        String orderCode = para.getOrderCode();//jsonObj.
        try {
            //iSpDailyPlanService.FlushFinishRate(orderCode);
            SpDailyPlan spDailyPlan = iSpDailyPlanService.getTodayPanByOrder(orderCode);
            float realFinishRate=iSpDailyPlanService.GenFinishRate(spDailyPlan);
            //耗时占比 ，或者理论完成率
            spDailyPlan.setExpectFinishRate( realFinishRate);// 耗时占比
            iSpDailyPlanService.saveOrUpdate(spDailyPlan);

            return Result.success("刷新完成率数据");
        }
        catch (Exception e) {
           return  Result.failure("查询失败 ，"+e.getMessage());
        }
    }



    @PostMapping("/addProduct")
    @ResponseBody
    public Result addProduct(@RequestBody SpProduct  spProduct) {
        try {
            //SpProduct  spProduct = new SpProduct();
            //iSpProductService.saveOrUpdate(spProduct);
            iSpProductService.AddProdut(spProduct);



            return Result.success("刷新完成率数据");
        }
        catch (Exception e) {
            return  Result.failure("失败 ，"+e.getMessage());
        }
    }


    @PostMapping("/updateProduct")
    @ResponseBody
    public Result updateProduct(@RequestBody SpProduct  spProduct) {
        try {

            iSpProductService.updateSpProductById(spProduct);
            return Result.success("完成率数据");
        }
        catch (Exception e) {
            return  Result.failure("失败 ，"+e.getMessage());
        }
    }


    @PostMapping("/updateTest")
    @ResponseBody
    public Result updateTest(@RequestBody SpTest spTest) {
        try {

            iSpTestService.saveOrUpdate(spTest);
            return Result.success("updateTest");
        }
        catch (Exception e) {
            return  Result.failure("失败 ，"+e.getMessage());
        }
    }




}
