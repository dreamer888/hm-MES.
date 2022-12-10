package com.lgl.mes.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lgl.mes.config.entity.SpConfig;
import com.lgl.mes.config.mapper.SpConfigMapper;
import com.lgl.mes.config.service.ISpConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-08-23
 */
@Service
public class SpConfigServiceImpl extends ServiceImpl<SpConfigMapper, SpConfig> implements ISpConfigService {


    @Autowired
    private SpConfigMapper spConfigMapper;

    @Autowired
    private ISpConfigService iSpConfigService;
    //只有在 NettyUDPServer  里面才不为空


    public  SpConfig GetCurrentConfig( String lineId) {
        QueryWrapper<SpConfig> queryWrapper = new QueryWrapper();
        queryWrapper.like("line_id", lineId);   //磁阻变压器
        SpConfig spConfig;

        //List<SpConfig> list = spConfigServiceImpl.list();
        List<SpConfig> list = iSpConfigService.list(queryWrapper);
        //List<SpConfig> list = iSpConfigService.list();
        spConfig = list.get(0);


        return  spConfig;
    }


}
