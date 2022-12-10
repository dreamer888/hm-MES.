package com.dream.iot.test;

import com.dream.iot.utils.IdWorker;
import org.apache.commons.lang3.RandomUtils;

/**
 * 客户端编号生成器
 */
public class ClientSnGen {

    private static IdWorker worker = new IdWorker(RandomUtils.nextInt(0, 32), RandomUtils.nextInt(0, 32));

    /**
     * 获取客户端编号
     * @return
     */
    public static long getClientSn() {
        return worker.nextId();
    }

    /**
     * 36进制编号
     * @return
     */
    public static String get36ClientSn() {
        return Long.toString(getClientSn(), 36).toUpperCase();
    }

    public static String getClientSn(String prefix) {
        return prefix + worker.nextId();
    }

    public static String getMessageId() {
        return worker.nextId() + "";
    }

}
