package com.lgl.mes.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lgl.mes.daily.entity.SpDailyPlan;
import com.lgl.mes.daily.service.ISpDailyPlanService;
import com.lgl.mes.globalId.entity.SpGlobalId;
import com.lgl.mes.globalId.service.ISpGlobalIdService;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.service.ISpOrderService;
import com.lgl.mes.product.entity.SpProduct;
import com.lgl.mes.product.mapper.SpProductMapper;
import com.lgl.mes.product.service.ISpProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgl.mes.technology.entity.SpFlow;
import com.lgl.mes.technology.service.ISpFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.lgl.mes.common.util.DateUtil.GetLocalDateTimeFromString;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgl
 * @since 2022-06-27
 */
@Service
public class SpProductServiceImpl extends ServiceImpl<SpProductMapper, SpProduct> implements ISpProductService {
    @Autowired
    private ISpFlowService iSpFlowService;


    @Autowired
    private ISpOrderService iSpOrderService;
    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;
    @Autowired
    private ISpProductService iSpProductService;

  @Autowired
  private   SpProductMapper spProductMapper;

    @Autowired
    private ISpGlobalIdService iSpGlobalIdService;


    public  Boolean  AddProdut(SpProduct record)
    {
        try {
            String jsonStr= JSONObject.toJSONString(record);
            System.out.println("record="+jsonStr);
            if (record.getId() == null || record.getId().isEmpty())  // add a new product  record
            {
                SpFlow spflow = iSpFlowService.getById(record.getFlowId());
                if (spflow != null)
                    record.setFlowDesc(spflow.getFlowDesc());

                iSpProductService.saveOrUpdate(record);  //保存产品记录  多个sql修改稿 操作， 应该使用transation

                /////////////////////////////////////
                QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
                String orderCode = record.getOrderNo();

                queryWrapper.eq("order_code", orderCode);
                List<SpOrder> list = iSpOrderService.list(queryWrapper);

                SpOrder spOrder = iSpOrderService.getById(list.get(0).getId()); //order_code其实也是唯一的
                spOrder.setMakedQty(spOrder.getMakedQty() + 1);
                if (record.getQuality().equals("NG") || record.getQuality().equals("不良品") || record.getQuality().equals("ng"))
                    spOrder.setBadQty(spOrder.getBadQty() + 1);

                spOrder.setFinishRate((float) ((float)spOrder.getMakedQty() *100.0f/ (float)spOrder.getPlanQty()));

                //spOrder.setFinishRate(new BigDecimal(spOrder.getMakedQty()).divide(new BigDecimal(spOrder.getPlanQty())));
                //////////更新订单数据
                iSpOrderService.saveOrUpdate(spOrder);


                SpDailyPlan spDailyPlan = iSpDailyPlanService.getTodayPanByOrder(orderCode);
                if (spDailyPlan != null) {

                    spDailyPlan.setMakedQty(spDailyPlan.getMakedQty() + 1);
                    if (record.getQuality().equals("NG") || record.getQuality().equals("不良品") || record.getQuality().equals("ng"))
                        spDailyPlan.setBadQty(spDailyPlan.getBadQty() + 1);
                    spDailyPlan.setFinishRate( (float)spDailyPlan.getMakedQty()*100.0f / (float)spDailyPlan.getPlanQty());
                    spDailyPlan.setPassRate( (float)(spDailyPlan.getMakedQty()-spDailyPlan.getBadQty())*100.0f / (float)spDailyPlan.getMakedQty());
                    //spDailyPlan.setPassRate();   //一次性通过率

                    //单件耗时

                    //最近一件的生产时间 ，
                    LocalDateTime  now = LocalDateTime.now();

                    if(spDailyPlan.getLastTime()!=null)
                    {
                        Long seconds = ChronoUnit.SECONDS.between(spDailyPlan.getLastTime(), now);
                        spDailyPlan.setRealPieceTime (Math.toIntExact(seconds));
                        spDailyPlan.setLastTime(now);
                    }
                    else {
                        spDailyPlan.setRealPieceTime ( spDailyPlan.getPieceTime());
                        spDailyPlan.setLastTime(now);
                    }


                    //理论完成率 =当日实际消耗时间/当日排产有效时间,  耗时占比
                    //LocalDateTime t = new LocalDateTime();
                  /*
                    Long dayEffetiveMinutes = Long.valueOf(0);
                    Long t1= Long.valueOf(0);
                    Long t2= Long.valueOf(0);
                    Long t3= Long.valueOf(0);
                    Long t4= Long.valueOf(0);
                    Long t5= Long.valueOf(0);
                    Long t6= Long.valueOf(0);
                    if(spDailyPlan.getMorningStart()!=null  && !spDailyPlan.getMorningStart().isEmpty() )
                     t1= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart()),GetLocalDateTimeFromString(spDailyPlan.getMorningEnd()));

                    if(spDailyPlan.getMorningStart1()!=null)
                     t2=ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart1()),GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1()));
                    if(spDailyPlan.getAfternoonStart()!=null&& !spDailyPlan.getAfternoonStart().isEmpty())
                     t3=  ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd()));
                    if(spDailyPlan.getAfternoonStart1()!=null && !spDailyPlan.getAfternoonStart1().isEmpty())
                     t4= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd1()));

                    if(spDailyPlan.getEveningStart()!=null  && !spDailyPlan.getEveningStart().isEmpty() )
                        t5=  ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart()),GetLocalDateTimeFromString(spDailyPlan.getEveningEnd()));
                    if(spDailyPlan.getEveningStart1()!=null&& !spDailyPlan.getEveningStart1().isEmpty() )
                        t6=  ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart1()),GetLocalDateTimeFromString(spDailyPlan.getEveningEnd1()));
                    dayEffetiveMinutes =t1+ t2+t3+t4+t5+t6;

                    ///////////////实际耗时////////////////
                    Long realEffetiveMinutes= Long.valueOf(0);

                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getMorningStart()),GetLocalDateTimeFromString(spDailyPlan.getMorningEnd())))
                            realEffetiveMinutes =  ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart()),now);
                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getMorningEnd()),GetLocalDateTimeFromString(spDailyPlan.getMorningStart1())))  //rest
                        realEffetiveMinutes=  t1;

                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getMorningStart1()),GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1())))
                        realEffetiveMinutes =ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart1()),now) +t1;
                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart())))  //rest
                        realEffetiveMinutes=  t1+t2;  //午休期间

                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd())))  //下午第一节
                        realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart()),now)+ t1+t2;  //
                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1())))  //
                        realEffetiveMinutes= t1+t2+t3;  //下午中间10分钟的短暂休息

                    //下午第二节
                    if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd1())))  //下午第二 节
                        realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1()),now)+ t1+t2+t3;  //


                    if ( spDailyPlan.getEveningStart()==null ) //没有夜班
                    {
                        realEffetiveMinutes= t1+t2+t3+t4;  //没有夜班，结束计算实际消耗时间
                    }


                    if ( spDailyPlan.getEveningStart()!=null && !spDailyPlan.getEveningStart().isEmpty()) //有夜班
                    {
                           if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd()),GetLocalDateTimeFromString(spDailyPlan.getEveningStart()))) //晚餐时段
                        realEffetiveMinutes= t1+t2+t3+t4;  //晚餐时间段

                        //夜班第一节
                        if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getEveningStart()),GetLocalDateTimeFromString(spDailyPlan.getEveningEnd())))
                            realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart()),now)+ t1+t2+t3+t4;  //

                        if ( spDailyPlan.getEveningStart1()!=null   && !spDailyPlan.getEveningStart1().isEmpty()) //有夜班第二节
                        {
                            if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getEveningEnd()),GetLocalDateTimeFromString(spDailyPlan.getEveningStart1())))
                                realEffetiveMinutes= t1+t2+t3+t4+t5;  //夜班中间休息
                            //夜班第二节
                            if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getEveningStart1()),GetLocalDateTimeFromString(spDailyPlan.getEveningEnd1())))
                                realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart1()),now)+ t1+t2+t3+t4+t5;
                        }
                    }////有夜班


                   */

                    float realFinishRate=iSpDailyPlanService.GenFinishRate(spDailyPlan);
                    //耗时占比 ，或者理论完成率
                    spDailyPlan.setExpectFinishRate( realFinishRate);// 耗时占比

                    iSpDailyPlanService.save(spDailyPlan);
                    //每生产一件产品刷新一次， 毕竟这个计算比较耗时，不宜频繁计算。那么中间休息的时间段效率保持不变，
                    // 一旦开始重新生产，就会重新计算实际生产率。

                }//if (spDailyPlan != null) {

            }//if (record.getId() == null || record.getId().isEmpty())  // add a new  record
            else {
                iSpProductService.saveOrUpdate(record);  //保存产品记录  多个sql修改稿 操作， 应该使用transation
                return true;
            }
        }
        catch ( Exception e )
        {
            return false;
        }

        return  true;
    }


    public  int updateSpProductById (SpProduct record)
    {
        spProductMapper.updateSpProductById(record);
        return 0;
    }

    public String  GetCurrentOrderCode()
    {
        QueryWrapper<SpGlobalId> queryWrapper = new QueryWrapper();
        queryWrapper.like("order_code", "");
        List<SpGlobalId> list = iSpGlobalIdService.list(queryWrapper);;

        SpGlobalId spGlobalId = iSpGlobalIdService.getById(list.get(0).getId()); //order_code其实也是唯一的
        return spGlobalId.getOrderCode();
    }

}
