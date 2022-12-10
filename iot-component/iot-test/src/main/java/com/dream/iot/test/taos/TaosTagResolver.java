package com.dream.iot.test.taos;

import com.dream.iot.taos.STable;
import com.dream.iot.taos.TagValue;
import com.dream.iot.taos.TagsResolver;
import org.springframework.stereotype.Component;

import java.sql.Types;

/**
 * 用来解析 Tags值
 * @see STable#tags()
 * @see STable#tagsResolver()
 * @see TaosBreakerUsingStable
 */
@Component
public class TaosTagResolver implements TagsResolver {

    private String[] tags = new String[] {"FJ.XM", "FJ.QZ", "FJ.ZZ", "FJ.LY", "FJ.SM", "FJ.FZ", "QZ.AX", "BeiJing", "ShangHai", "ShenZhen"};

    @Override
    public TagValue resolve(String tableName, String tagName) {
        String index = tableName.substring(tableName.length() - 1);
        return new TagValue(tags[Integer.valueOf(index)], Types.NCHAR);
    }
}
