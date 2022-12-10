package com.dream.iot.client;

import com.dream.iot.*;
import com.dream.iot.codec.filter.CombinedFilter;
import org.springframework.core.GenericTypeResolver;

import java.util.Optional;

public interface ClientComponent<M extends ClientMessage> extends FrameworkComponent, MultiClientManager {

    /**
     * 获取默认客户端配置
     * @return {ClientConnectProperties or null}
     */
    ClientConnectProperties getConfig();

    /**
     * 获取默认客户端
     * @return {IotClient or null}
     */
    IotClient getClient();

    /**
     * 连接服务器
     */
    void connect();

    /**
     * 获取指定的客户端
     * @param clientKey 客户端标识
     * @return
     */
    IotClient getClient(Object clientKey);

    /**
     * 移除指定的客户端
     * @param clientKey
     */
    IotClient removeClient(Object clientKey);

    /**
     * 创建一个新客户端
     * @param config
     * @return
     */
    IotClient createNewClient(ClientConnectProperties config);

    @Override
    default Class<M> getMessageClass() {
        return (Class<M>) GenericTypeResolver.resolveTypeArgument(getClass(), ClientComponent.class);
    }

    @Override
    Optional<CombinedFilter> getFilter();

    @Override
    default ClientComponent getClientComponent() {
        return this;
    }
}
