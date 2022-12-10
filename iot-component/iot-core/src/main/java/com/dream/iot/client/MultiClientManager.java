package com.dream.iot.client;

import java.util.List;

/**
 * 多客户端管理, 比如使用在分布式集群
 */
public interface MultiClientManager {

    /**
     * 新增客户端
     * @param clientKey
     */
    void addClient(Object clientKey, IotClient value);

    /**
     * 获取客户端
     * @param clientKey
     * @return
     */
    IotClient getClient(Object clientKey);

    /**
     * 移除客户端
     * @param clientKey
     * @return
     */
    IotClient removeClient(Object clientKey);

    /**
     * 获取客户端列表
     * @return
     */
    List<IotClient> clients();

    ClientComponent getClientComponent();

    void setClientComponent(ClientComponent component);
}
