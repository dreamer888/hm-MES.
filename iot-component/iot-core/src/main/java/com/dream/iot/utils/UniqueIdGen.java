package com.dream.iot.utils;

import org.apache.commons.lang3.RandomUtils;

/**
 * 唯一数据生成器
 */
public final class UniqueIdGen {

    private static IdWorker worker = new IdWorker(RandomUtils.nextInt(0, 32), RandomUtils.nextInt(0, 32));

    public static long nextLong() {
        return worker.nextId();
    }

    /**
     * 唯一messageId
     * @return
     */
    public static String messageId() {
        return "MI" + worker.nextId();
    }

    public static String messageId(String prefix) {
        return prefix + worker.nextId();
    }

    /**
     * 唯一设备编号
     * @return
     */
    public static String deviceSn() {
        return "DS" + worker.nextId();
    }
}
