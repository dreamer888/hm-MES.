package com.dream.iot;

import com.dream.iot.codec.filter.CombinedFilter;

import java.util.Optional;

/**
 * 用来声明和设备对接时用来管理设备的设备组件
 */
public interface FrameworkComponent {
    /**
     * 组件功能名称
     * @return
     */
    String getName();

    /**
     * 设备服务说明
     * @return
     */
    String getDesc();

    /**
     * 开启时间
     * @return
     */
    long startTime();

    /**
     * 初始化组件
     * @param args
     */
    void init(Object ...args);

    /**
     * 组件初始化完成
     * 在spring容器启动完成并且netty和框架的组件都加载和初始化完成之后调用
     */
    default void finished() {}

    /**
     * 组件流程过滤器
     * @return
     */
    Optional<CombinedFilter> getFilter();

    /**
     * 设备相关协议的协议工厂
     * @return
     */
    IotProtocolFactory protocolFactory();

    /**
     * 对应设备的报文Class对象
     * @see SocketMessage
     * @return
     */
    Class<? extends Message> getMessageClass();
}
