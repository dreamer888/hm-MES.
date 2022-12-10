package com.dream.iot.test.db.line.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dream.iot.test.db.line.entity.SpLine;

import java.util.List;

/**
 * <p>
 * 线体表 Mapper 接口
 * </p>
 *
 * @author 75039960@qq.com
 * @since 2022-07-01
 */
public interface SpLineMapper extends BaseMapper<SpLine> {

    List<SpLine> GetLineList();


}
