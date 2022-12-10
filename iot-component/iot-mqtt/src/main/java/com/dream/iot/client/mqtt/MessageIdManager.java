package com.dream.iot.client.mqtt;

import com.dream.iot.StorageManager;

public interface MessageIdManager<K> extends StorageManager<K, MessageMapper> {

    /**
     * 客户端PacketId前缀
     */
    String CLIENT_PREFIX = "CLIENT:";

    /**
     * 服务端PacketId前缀
     */
    String SERVER_PREFIX = "SERVER:";

    /**
     * 获取下一个messageId
     * @return
     */
    int nextId();

    /**
     * 过期处理
     * 定时校验未确认的报文, 重发或者移除
     */
    void expire();

    /**
     * 验证客户端是否包含此packetId
     * @param packetId
     * @return
     */
    @Override
    boolean isExists(K packetId);

    @Override
    MessageMapper get(K packetId);

    @Override
    MessageMapper add(K packetId, MessageMapper mapper);

    @Override
    MessageMapper remove(K packetId);

    /**
     * 获取服务端的packetId对应的报文
     * @param packetId
     * @return
     */
    MessageMapper getServer(Integer packetId);

    /**
     * 增加服务端的PacketId对应的报文信息
     * @param packetId 服务端packetId
     * @param mapper
     * @return
     */
    MessageMapper addServer(Integer packetId, MessageMapper mapper);

    /**
     * 移除服务端packetId对应的报文信息
     * @param packetId
     * @return
     */
    MessageMapper removeServer(Integer packetId);
}
