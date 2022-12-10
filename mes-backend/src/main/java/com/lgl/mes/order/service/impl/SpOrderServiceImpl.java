package com.lgl.mes.order.service.impl;

import com.lgl.mes.common.Result;
import com.lgl.mes.line.entity.SpLine;
import com.lgl.mes.line.mapper.SpLineMapper;
import com.lgl.mes.order.entity.SpOrder;
import com.lgl.mes.order.mapper.SpOrderMapper;
import com.lgl.mes.order.service.ISpOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dreamer,75039960@qq.com
 * @since 2022-06-01
 */
@Service
public class SpOrderServiceImpl extends ServiceImpl<SpOrderMapper, SpOrder> implements ISpOrderService {

    //private SpLineMapper spLineMapper;

    @Autowired
    SpLineMapper spLineMapper;

    public Result getLines() throws Exception {
        //List<SpLine > list = new ArrayList<>();
        //spLineMapper = new SpLineMapper();
        List<SpLine> list = spLineMapper.GetLineList();

        return Result.success(list);

    }



}
