package com.dream.iot.test.db.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dream.iot.test.db.daily.entity.SpDailyPlan;
import com.dream.iot.test.db.daily.service.ISpDailyPlanService;
import com.dream.iot.test.db.globalId.entity.SpGlobalId;
import com.dream.iot.test.db.globalId.service.ISpGlobalIdService;
import com.dream.iot.test.db.order.entity.SpOrder;
import com.dream.iot.test.db.order.service.ISpOrderService;
import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.mapper.SpProductMapper;
import com.dream.iot.test.db.product.service.ISpProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
   // @Autowired
    //private ISpFlowService iSpFlowService;


    @Autowired
    private ISpOrderService iSpOrderService;
    @Autowired
    private ISpDailyPlanService iSpDailyPlanService;
    @Autowired
    private ISpProductService iSpProductService;

    @Autowired
    private ISpGlobalIdService iSpGlobalIdService;



  @Autowired
  private   SpProductMapper spProductMapper;

    Logger log = LoggerFactory.getLogger(SpProductServiceImpl.class);

    public String  GetCurrentOrderCode(String line)
    {
        QueryWrapper<SpGlobalId> queryWrapper = new QueryWrapper();
        queryWrapper.eq("line", line);
        List<SpGlobalId> list = iSpGlobalIdService.list(queryWrapper);;

        SpGlobalId spGlobalId = iSpGlobalIdService.getById(list.get(0).getId()); //order_code其实也是唯一的
        return spGlobalId.getOrderCode();
    }

    /*
     添加了 GetCurrentOrderCode
     */
    public  Boolean  AddProdut(SpProduct record)
    {
        try {
            if (record.getId() == null || record.getId().isEmpty())  // add a new product  record
            {
      /*          SpFlow spflow = iSpFlowService.getById(record.getFlowId());
                if (spflow != null)
                    record.setFlowDesc(spflow.getFlowDesc());
*/
                QueryWrapper<SpProduct> queryWrappe0 = new QueryWrapper();
                queryWrappe0.eq("product_id",record.getProductId());
                queryWrappe0.eq("serial_id",record.getSerialId());
                List<SpProduct> list0 = iSpProductService.list(queryWrappe0);
                if( list0 !=null &&list0.size()>0)   //避免重复产品
                    return false;

                String orderCode = GetCurrentOrderCode(record.getFlowId());
                record.setOrderNo(orderCode);

                iSpProductService.saveOrUpdate(record);  //保存产品记录  多个sql修改稿 操作， 应该使用transation
                System.out.println("保存产品记录,完成");
                /////////////////////////////////////
                QueryWrapper<SpOrder> queryWrapper = new QueryWrapper();
                //String orderCode = record.getOrderNo();

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
                System.out.println("更新订单数据完成");

                SpDailyPlan spDailyPlan = iSpDailyPlanService.getTodayPanByOrder(spOrder);
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

                  float realFinishRate=iSpDailyPlanService.GenFinishRate(spDailyPlan);
                    //耗时占比 ，或者理论完成率
                    spDailyPlan.setExpectFinishRate( realFinishRate);// 耗时占比

                    iSpDailyPlanService.saveOrUpdate(spDailyPlan);

                    //System.out.println("更新每日计划记录,完成");
                    log.info("更新每日计划记录,完成");


                    //每生产一件产品刷新一次， 毕竟这个计算比较耗时，不宜频繁计算。那么中间休息的时间段效率保持不变，
                    // 一旦开始重新生产，就会重新计算实际生产率。

                }//if (spDailyPlan != null) {

            }//if (record.getId() == null || record.getId().isEmpty())  // add a new  record
            else
            {   //update
                iSpProductService.saveOrUpdate(record);
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

}
