package com.lgl.mes.daily.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lgl.mes.daily.entity.SpDailyPlan;
import com.lgl.mes.daily.mapper.SpDailyPlanMapper;
import com.lgl.mes.daily.service.ISpDailyPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.service.ISpOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.lgl.mes.common.util.DateUtil.GetLocalDateTimeFromString;
import static com.lgl.mes.common.util.DateUtil.IsInScope;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-02
 */
@Service
public class SpDailyPlanServiceImpl extends ServiceImpl<SpDailyPlanMapper, SpDailyPlan> implements ISpDailyPlanService {

    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;
    @Autowired
    private ISpOrderService iSpOrderService;
    public  SpDailyPlan  getTodayPanByOrder(String orderCode)
    {
        try {
            QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_code", orderCode);
            SpOrder spOrder = iSpOrderService.getOne(queryWrapper);
            QueryWrapper<SpDailyPlan> queryWrapperDaily = new QueryWrapper();
            queryWrapperDaily.eq("order_code", orderCode);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);
            queryWrapperDaily.like("plan_date", dateString);

            SpDailyPlan spDailyPlan = iSpDailyPlanService.getOne(queryWrapperDaily);
            if(spDailyPlan!=null)
            return  spDailyPlan;
        }
        catch (Exception e)
        {
            return  null;
        }

        return  null;
    }


    /**
     * 计算实际生产率，perfect,
     * 生产第一件产品之前 ，耗时按0计算
     * @param spDailyPlan
     * @return
     */
    public float GenFinishRate(SpDailyPlan spDailyPlan) {
        Long dayEffetiveMinutes = Long.valueOf(0);
        Long t1 = Long.valueOf(0);
        Long t2 = Long.valueOf(0);
        Long t3 = Long.valueOf(0);
        Long t4 = Long.valueOf(0);
        Long t5 = Long.valueOf(0);
        Long t6 = Long.valueOf(0);
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime  tEnd = null;

        if (spDailyPlan.getMorningStart() != null && !spDailyPlan.getMorningStart().isEmpty()) {
            t1 = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart()), GetLocalDateTimeFromString(spDailyPlan.getMorningEnd()));
            tEnd = GetLocalDateTimeFromString(spDailyPlan.getMorningEnd());
        }
        if (spDailyPlan.getMorningStart1() != null && !spDailyPlan.getMorningStart1().isEmpty())
        {   t2 = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart1()), GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1()));
            tEnd = GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1());
        }
        if (spDailyPlan.getAfternoonStart() != null && !spDailyPlan.getAfternoonStart().isEmpty())
        {      t3 = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart()), GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd()));
            tEnd = GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd());
        }


        if (spDailyPlan.getAfternoonStart1() != null && !spDailyPlan.getAfternoonStart1().isEmpty())
        {
            t4 = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1()), GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd1()));
            tEnd = GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd1());
        }

        if (spDailyPlan.getEveningStart() != null && !spDailyPlan.getEveningStart().isEmpty())
        {
            t5 = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart()), GetLocalDateTimeFromString(spDailyPlan.getEveningEnd()));
            tEnd = GetLocalDateTimeFromString(spDailyPlan.getEveningEnd());
        }


        if (spDailyPlan.getEveningStart1() != null && !spDailyPlan.getEveningStart1().isEmpty()) {
            t6 = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart1()), GetLocalDateTimeFromString(spDailyPlan.getEveningEnd1()));
            tEnd = GetLocalDateTimeFromString(spDailyPlan.getEveningEnd1());
        }
        dayEffetiveMinutes = t1 + t2 + t3 + t4 + t5 + t6;

        if (dayEffetiveMinutes == 0)
            return -1.0f;

        ///////////////实际耗时////////////////
        Long realEffetiveMinutes = Long.valueOf(0);


        if (spDailyPlan.getMorningStart() != null && !spDailyPlan.getMorningStart().isEmpty()) {
            if (now.isBefore(GetLocalDateTimeFromString(spDailyPlan.getMorningStart()))) {
                realEffetiveMinutes = Long.valueOf(0);
                return (float) (realEffetiveMinutes * 100.0f) / dayEffetiveMinutes;

            }
            ;
        }

        if (spDailyPlan.getMorningEnd() != null && !spDailyPlan.getMorningEnd().isEmpty())
            if (IsInScope(now, GetLocalDateTimeFromString(spDailyPlan.getMorningStart()), GetLocalDateTimeFromString(spDailyPlan.getMorningEnd()))) {
                realEffetiveMinutes = ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart()), now);
                return (float) (realEffetiveMinutes * 100.0f) / dayEffetiveMinutes;
            }
        if (spDailyPlan.getMorningStart1() != null && !spDailyPlan.getMorningStart1().isEmpty())
            if (IsInScope(now, GetLocalDateTimeFromString(spDailyPlan.getMorningEnd()), GetLocalDateTimeFromString(spDailyPlan.getMorningStart1())))  //rest
            {
                realEffetiveMinutes = t1;
                return (float) (realEffetiveMinutes * 100.0f) / dayEffetiveMinutes;
            }

        if (spDailyPlan.getMorningEnd1() != null && !spDailyPlan.getMorningEnd1().isEmpty())
            if (IsInScope(now, GetLocalDateTimeFromString(spDailyPlan.getMorningStart1()), GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1())))
            {
                realEffetiveMinutes =ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getMorningStart1()),now) +t1;
                return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
            }
        if(spDailyPlan.getAfternoonStart()!=null  && !spDailyPlan.getAfternoonStart().isEmpty() )
        if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getMorningEnd1()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart())))  //rest
        {
            realEffetiveMinutes=  t1+t2;  //午休期间
            return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
        }
        if(spDailyPlan.getAfternoonEnd()!=null  && !spDailyPlan.getAfternoonEnd().isEmpty() )
        if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd())))  //下午第一节
        {   realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart()),now)+ t1+t2;  //
            return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
        }
        if(spDailyPlan.getAfternoonStart1()!=null  && !spDailyPlan.getAfternoonStart1().isEmpty() )
        if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1())))  //
        {   realEffetiveMinutes= t1+t2+t3;  //下午中间10分钟的短暂休息
            return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
        }
        //下午第二节
        if(spDailyPlan.getAfternoonStart1()!=null  && !spDailyPlan.getAfternoonStart1().isEmpty() )
        if (IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1()),GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd1())))  //下午第二 节
        {   realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getAfternoonStart1()),now)+ t1+t2+t3;  //

            return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
        }
        if(spDailyPlan.getEveningStart()!=null  && !spDailyPlan.getEveningStart().isEmpty() )
            if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getAfternoonEnd()),GetLocalDateTimeFromString(spDailyPlan.getEveningStart()))) //晚餐时段
            {       realEffetiveMinutes= t1+t2+t3+t4;  //晚餐时间段
                return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
            }
            //夜班第一节
        if(spDailyPlan.getEveningStart()!=null  && !spDailyPlan.getEveningStart().isEmpty() )
            if( IsInScope(now,      GetLocalDateTimeFromString(spDailyPlan.getEveningStart()),GetLocalDateTimeFromString(spDailyPlan.getEveningEnd())))
            {   realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart()),now)+ t1+t2+t3+t4;  //
                return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
            }
        if(spDailyPlan.getEveningStart1()!=null  && !spDailyPlan.getEveningStart1().isEmpty() )
            if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getEveningEnd()),GetLocalDateTimeFromString(spDailyPlan.getEveningStart1())))
            {   realEffetiveMinutes= t1+t2+t3+t4+t5;  //夜班中间休息
                return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
            }
                //夜班第二节
        if(spDailyPlan.getEveningStart1()!=null  && !spDailyPlan.getEveningStart1().isEmpty() )
        if( IsInScope(now,GetLocalDateTimeFromString(spDailyPlan.getEveningStart1()),GetLocalDateTimeFromString(spDailyPlan.getEveningEnd1())))
        {
            realEffetiveMinutes= ChronoUnit.MINUTES.between(GetLocalDateTimeFromString(spDailyPlan.getEveningStart1()),now)+ t1+t2+t3+t4+t5;
            return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
        }

        if(now.isAfter(tEnd))
        {
            realEffetiveMinutes= ChronoUnit.MINUTES.between(tEnd,now)+ t1+t2+t3+t4+t5+t6;
            return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;
        }


        //耗时占比 ，或者理论完成率
        //spDailyPlan.setExpectFinishRate( (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes);// 耗时占比
        // 不能使用 BigDecimal
        //iSpDailyPlanService.saveOrUpdate(spDailyPlan); //can not saveOrUpdate ,why ?

        return (float) (realEffetiveMinutes*100.0f)/dayEffetiveMinutes;


    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public  Boolean  FlushFinishRate(String  orderCode)
    {
        try {
            SpDailyPlan spDailyPlan = iSpDailyPlanService.getTodayPanByOrder(orderCode);
            float realFinishRate=iSpDailyPlanService.GenFinishRate(spDailyPlan);
            //耗时占比 ，或者理论完成率
            spDailyPlan.setExpectFinishRate( realFinishRate);// 耗时占比
            iSpDailyPlanService.saveOrUpdate(spDailyPlan);

        }
        catch ( Exception e )
        {
            return false;
        }
        return  true;
    }




}
