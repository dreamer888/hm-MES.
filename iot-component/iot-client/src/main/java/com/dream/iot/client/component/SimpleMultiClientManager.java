package com.dream.iot.client.component;

import com.dream.iot.ConcurrentStorageManager;
import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.IotClient;
import com.dream.iot.client.MultiClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的客户端管理实现
 */
public class SimpleMultiClientManager extends ConcurrentStorageManager<String, IotClient> implements MultiClientManager {

    private ClientComponent clientComponent;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public SimpleMultiClientManager(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    @Override
    public synchronized void addClient(Object clientKey, IotClient value) {
        String clientKeyStr;
        if(clientKey instanceof ClientConnectProperties) {
            clientKeyStr = ((ClientConnectProperties) clientKey).connectKey();
        } else {
            clientKeyStr = clientKey.toString();
        }

        if(!isExists(clientKeyStr)) {
            add(clientKeyStr, value);
        } else {
            if(logger.isDebugEnabled()) {
                logger.warn("客户端管理({}) 客户端已存在 - 客户端标识：{} - 当前数量：{}", this.clientComponent.getName(), clientKeyStr, size());
            }
        }
    }

    @Override
    public IotClient getClient(Object clientKey) {
        String clientKeyStr;
        if(clientKey instanceof ClientConnectProperties) {
            clientKeyStr = ((ClientConnectProperties) clientKey).connectKey();
        } else {
            clientKeyStr = clientKey.toString();
        }

        return get(clientKeyStr);
    }

    @Override
    public IotClient removeClient(Object clientKey) {
        String clientKeyStr;
        if(clientKey instanceof ClientConnectProperties) {
            clientKeyStr = ((ClientConnectProperties) clientKey).connectKey();
        } else {
            clientKeyStr = clientKey.toString();
        }

        return remove(clientKeyStr);
    }

    @Override
    public List<IotClient> clients() {
        return new ArrayList<>(getMapper().values());
    }

    @Override
    public ClientComponent getClientComponent() {
        return this.clientComponent;
    }

    @Override
    public void setClientComponent(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

}
