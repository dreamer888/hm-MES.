package com.dream.iot.client.mqtt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

public class ClientIdGen {

    private static final String BASIC_CHAR = "iot0123456789eaj";

    public synchronized static String genClientId() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmssSS") + "_" + RandomUtil.randomString(BASIC_CHAR, 6);
    }
}
