package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.Protocol;
import com.dream.iot.handle.proxy.ProtocolHandleProxy;

/**
 * <h>mqtt处理器</h>
 * 用于将其他客户端采集的数据发布到mqtt服务器
 * @param <T>
 */
public interface MqttGatewayHandle<T extends Protocol, E> extends ProtocolHandleProxy<T> {

    /**
     * 自定义报文头
     * @return
     */
    default MqttGatewayHead getMqttGatewayHead(E entity) {
        return new MqttGatewayHead(getEquipCode(entity));
    }

    default String getEquipCode(E entity) {
        return getClass().getName();
    }

    /**
     * mqtt服务器配置
     * @param entity {@link #handle(Protocol)}的返回值
     * @return 返回的连接信息即要发布的mqtt服务器配置信息 根据{@link MqttGatewayConnectProperties#getClientId()}标识不同的连接
     */
    MqttGatewayConnectProperties getProperties(E entity);
}
