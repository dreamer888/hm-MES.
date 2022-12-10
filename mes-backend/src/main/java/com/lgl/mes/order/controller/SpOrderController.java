package com.lgl.mes.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lgl.mes.basedata.entity.SpTableManager;
import com.lgl.mes.order.request.spOrderReq;
import com.lgl.mes.common.BaseController;
import com.lgl.mes.common.Result;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.service.ISpOrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lgl.mes.common.util.DateUtil;

import  com.lgl.mes.line.mapper.SpLineMapper;
import  com.lgl.mes.line.entity.SpLine;

/**
 * 工单控制器
 *
 * @author dreamer,75039960@qq.com
 * @since 2022-06-01
 *
 * 把一天工作时间划分为5个段落，比如早上8点到10，休息10分钟，10点10分到11点40，
 * 开始午饭午休，
 * 下午1点上班，到3点休息10分钟，3点10分到5点40下班。
 * 从晚上6点10分开始 加班， 加班到几点不确定，设置一个默认值，到晚上8点，
 * 可以在每日计划中修改
 */
@Controller
@RequestMapping("/order")
public class SpOrderController extends BaseController {

    @Autowired
    private ISpOrderService iSpOrderService;

    @Autowired
    private SpLineMapper spLineMapper;

    /**
     * 生产订单管理界面
     *
     * @param model 模型
     * @return 生产订单管理界面
     */
    @ApiOperation("生产订单管理界面界面UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/list-ui")
    public String listUI(Model model) {
        return "/order/list";
    }

    @ApiOperation("gantt")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/gantt")
    public String gantt(Model model) {
        return "/gantt/index";
    }


    /**
     * 生产订单修改界面
     *
     * @param model  模型
     * @param record 平台表对象
     * @return 更改界面
     */
    @ApiOperation("生产订单新增和修改界面")
    @GetMapping("/add-or-update-ui")
    public String addOrUpdateUI(Model model, SpTableManager record) {
        if (StringUtils.isNotEmpty(record.getId())) {
            SpOrder spOrder = iSpOrderService.getById(record.getId());
            spOrder.setPlanStartTime(DateUtil.GetTString(spOrder.getPlanStartTime()));
            spOrder.setPlanEndTime(DateUtil.GetTString(spOrder.getPlanEndTime()));
            model.addAttribute("result", spOrder);
        }
        return "/order/addOrUpdate";
    }


    /**
     * 生产订单界面分页查询
     *
     * @param req 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("生产订单界界面分页查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "req", value = "请求参数", defaultValue = "请求参数")})
    @PostMapping("/page")
    @ResponseBody
    public Result page(spOrderReq req) {
        QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(req.getOrderCode())) {
            queryWrapper.like("order_code", req.getOrderCode());
        }

        IPage result = iSpOrderService.page(req, queryWrapper);
        return Result.success(result);
    }

    /**
     * 生产订单界修改、新增
     *
     * @param record 订单实体类
     * @return 执行结果
     */
    @ApiOperation("生产订单界修改、新增")
    @PostMapping("/add-or-update")
    @ResponseBody
    public Result addOrUpdate(SpOrder record) {
    //front 2022-07-10T15:30'

        if(record.getPlanStartTime()!=null )
            record.setPlanStartTime( DateUtil.GetStampString(record.getPlanStartTime().toString()));

        if(record.getPlanEndTime()!=null )
            record.setPlanEndTime( DateUtil.GetStampString(record.getPlanEndTime().toString()));

        iSpOrderService.saveOrUpdate(record);
        return Result.success();
    }


    /**
     * 删除生产订单界
     *
     * @param record 请求参数
     * @return Result 执行结果
     */
    @ApiOperation("删除生产订单界")
    @ApiImplicitParams({@ApiImplicitParam(name = "record", value = "订单实体", defaultValue = "订单实体")})
    @PostMapping("/delete")
    @ResponseBody
    public Result deleteByOrderId(SpOrder record) throws Exception {
        iSpOrderService.removeById(record.getId());
        return Result.success();
    }

    @ResponseBody
    @RequestMapping(value = "/gantt/list1", method = RequestMethod.POST, produces = "application/json")
//    public Result getListGantt(Map<String, Object> params) throws Exception {
    public Result getListGanttBak(spOrderReq req) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        //long  ns = 24*60*60*1000*1000;
        long UTC_DAY_IN_MS = 24 * 60 * 60 * 1000;
        //具体的订单信息
        for (int i = 0; i < 20; i++) {
            Map<String, Object> map = new HashMap<>(8);
            Map<String, Object> value = new HashMap<>(8);
            List<Map<String, Object>> values = new ArrayList<>();
            if (i % 2 == 0) {
                long len = i *UTC_DAY_IN_MS;//100000 * i;  //2000000000   //目前那就是20天
                map.put("id", "id" + (i + 1));
                map.put("name", "除湿器一线：工单号" + (i + 1));
                map.put("desc", "计划数量： " );
                value.put("from", "/Date(" + System.currentTimeMillis() + ")/");
                value.put("to", "/Date(" + (System.currentTimeMillis() + len)+ ")/");
                value.put("label", "华夏除湿器");
                value.put("desc", "完工进度100%");
                value.put("customClass", "ganttGreen");
                value.put("dataObj", ""+i);
                values.add(value);
            } else {
                map.put("desc", "完工数量：0 "+ req.getOrderCode());
            }
            map.put("cssClass", "redLabel");
            map.put("values", values);
            result.add(map);
        }
        return Result.success(result);
    }


    /*
    Timestamp.getTime  返回1970 以来的毫秒
     */
    @ResponseBody
    @RequestMapping(value = "/gantt/list", method = RequestMethod.POST, produces = "application/json")
//    public Result getListGantt(Map<String, Object> params) throws Exception {
    public Result getListGantt(spOrderReq req) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        //long  ns = 24*60*60*1000*1000;
        long UTC_DAY_IN_MS = 24 * 60 * 60 * 1000;

        QueryWrapper<SpOrder> queryWrapper =new QueryWrapper();
        if (StringUtils.isNotEmpty(req.getOrderCode()))
        {
            queryWrapper.like("order_code",req.getOrderCode());
        }
        if (StringUtils.isNotEmpty(req.getStatus()) && !req.getStatus().equals("所有"))
        {
            queryWrapper.like("status",req.getStatus());
        }

        List<Map<String, Object>>  records = iSpOrderService.listMaps(queryWrapper);

        for (int i = 0; i < records.size(); i++) {
            Map<String, Object> map = new HashMap<>(8);
            Map<String, Object> value = new HashMap<>(8);
            List<Map<String, Object>> values = new ArrayList<>();
            //SpOrder spOrder = (SpOrder)result.get(i).get("xxx");
            Map<String, Object> spOrderMap = records.get(i);

            Timestamp  t1 =(Timestamp)  Timestamp.valueOf(spOrderMap.get("plan_start_time").toString());
            Timestamp  t2 =(Timestamp)  Timestamp.valueOf(spOrderMap.get("plan_end_time").toString());
            map.put("id", spOrderMap.get("id"));
            map.put("name","产线:"+spOrderMap.get("flow"));
            //map.put("desc", "订单编号: "+ spOrderMap.get("order_code")+" 计划数量: "+ spOrderMap.get("plan_qty") );
            map.put("desc", "订单编号:"+ spOrderMap.get("order_code") );
            value.put("from", "/Date(" + t1.getTime()+ ")/");
            value.put("to", "/Date(" + t2.getTime()+ ")/");
            value.put("label", spOrderMap.get("product"));
            int plan_qty = Integer.valueOf(spOrderMap.get("plan_qty").toString());
            int maked_qty = Integer.parseInt(spOrderMap.get("maked_qty").toString());
            float makeRate=0;
            if(plan_qty!=0) makeRate = (float) ((float)maked_qty/ (float)plan_qty )*100  ;
            value.put("desc", "完工进度"+ makeRate+"%");
            String color="ganttGreen";
            String status = spOrderMap.get("status").toString();
            if(status.equals("进行中")) color = "ganttBlue";
            else if(status.equals("订单结束")) color = "ganttBlack";
            else if(status.equals("订单终结")) color = "ganttRed";
            value.put("customClass", color);
            value.put("dataObj", spOrderMap.get("id"));
            values.add(value);

            map.put("cssClass", "redLabel");
            map.put("values", values);
            result.add(map);

            //////////////////
            //Map<String, Object> value2 = new HashMap<>(8);
            //List<Map<String, Object>> values2 = new ArrayList<>();
            Map<String, Object> map2 = new HashMap<>(8);
            map2.put("desc", " 计划:"+ spOrderMap.get("plan_qty")+" 完工:"+ spOrderMap.get("maked_qty")+" 次品:"+ spOrderMap.get("bad_qty"));
            map2.put("cssClass", "redLabel");
            map2.put("values", null);
            result.add(map2);
        }

        return Result.success(result);
    }


    /**
     *
     * @param
     * @return     1,创建 2 进行中，3订单结束，4订单终结
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "status", method = RequestMethod.GET, produces = "application/json")
    public Result getOrderStatus() throws Exception {
        List<String > list = new ArrayList<>();
        list.add("创建");
        list.add("进行中");
        list.add("订单结束");
        list.add("订单终结");
        //list.add("所有");
        return Result.success(list);

    }

    @ResponseBody
    @RequestMapping(value = "status2", method = RequestMethod.GET, produces = "application/json")
    public Result getOrderStatus2() throws Exception {
        List<String > list = new ArrayList<>();
        list.add("创建");
        list.add("进行中");
        list.add("订单结束");
        list.add("订单终结");
        list.add("所有");
        return Result.success(list);

    }


    @ResponseBody
    @RequestMapping(value = "getLines", method = RequestMethod.GET, produces = "application/json")
    public Result getLines() throws Exception {
        //List<SpLine > list = new ArrayList<>();

        List<SpLine> list = spLineMapper.GetLineList();

        return Result.success(list);

    }


    /**
     * ==========当日计划========================
     * @param model
     * @return
     */
    @ApiOperation("生产订单当日计划 UI")
    @ApiImplicitParams({@ApiImplicitParam(name = "model", value = "模型", defaultValue = "模型")})
    @GetMapping("/today-ui")
    public String todayUI(Model model) {
        return "/order/today";
    }



}
