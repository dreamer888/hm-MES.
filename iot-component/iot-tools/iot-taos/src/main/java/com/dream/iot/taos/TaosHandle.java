package com.dream.iot.taos;

import com.dream.iot.Protocol;
import com.dream.iot.handle.proxy.ProtocolHandleProxy;

public interface TaosHandle<T extends Protocol> extends ProtocolHandleProxy<T>, IotTaos {

    String DEFAULT_TEMPLATE = "jdbcTemplate";

    /**
     * @see TaosEntity
     * @param protocol
     * @return 返回的对象必须是 {@link TaosEntity}的子类
     */
    @Override
    Object handle(T protocol);

    /**
     * 每次批量写入时的最大条数
     * @return 1 说明立马入库, >1 将使用批量写入的方式
     */
    default int maxOfPeer() {
        return 1;
    }

    /**
     * 入库周期(秒) 如果条数没有达到{@link #maxOfPeer()}时 多久入库一次
     * @see #maxOfPeer() > 1 时生效
     * @return 默认10秒
     */
    default long period() {
        return 10;
    }

    /**
     * 使用的jdbc模板, 用来支持多Taos数据源
     * @return jdbcTemplate名称, 通过spring容器获取
     */
    default String jdbcTemplate() {
        return DEFAULT_TEMPLATE;
    }
}
