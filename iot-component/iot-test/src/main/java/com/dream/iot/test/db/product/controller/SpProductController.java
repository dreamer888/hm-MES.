package com.dream.iot.test.db.product.controller;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.dream.iot.test.db.common.Result;
import com.dream.iot.test.db.daily.service.ISpDailyPlanService;
import com.dream.iot.test.db.order.request.spOrderReq;
import com.dream.iot.test.db.order.service.ISpOrderService;
import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.request.spProductReq;
import com.dream.iot.test.db.product.service.ISpProductService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dreamer  75039960@qq.com
 * @since 2022-06-27
 */
@Controller
@RequestMapping("/product")
public class SpProductController extends BaseController {

    @Autowired
    private ISpProductService iSpProductService;

    @Autowired
    private ISpOrderService iSpOrderService;
    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;


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



    /**
     * 该订单的产品列表  按时间顺序取 ,limit 30
     * @param para
     * @return
     */
    @PostMapping("/productList")
    @ResponseBody
    public Result productList(@RequestBody spOrderReq para) {
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




    @GetMapping("/getActualProcess")
    @ResponseBody
    public Result getActualProcess() {

        DateTime dt= new DateTime();
        String sdt = dt.toString("yyyy-MM-dd hh:mm:ss");
        Number n = (dt.getSeconds()%60) ;

        return Result.success(n,"当前进度"); //uccessData(200, n).put("msg", "当前进度");
    }


}
