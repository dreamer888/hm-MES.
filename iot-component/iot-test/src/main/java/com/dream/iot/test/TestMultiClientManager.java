package com.dream.iot.test;

import com.dream.iot.ConcurrentStorageManager;
import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.IotClient;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.config.ConnectProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用来做多客户端测试(多客户端)
 */
public class TestMultiClientManager extends ConcurrentStorageManager<String, IotClient> implements MultiClientManager {

    private ClientComponent clientComponent;

    @Override
    public void addClient(Object clientKey, IotClient value) {
        this.add(getClientKey(clientKey), value);
    }

    @Override
    public IotClient getClient(Object clientKey) {
        return get(this.getClientKey(clientKey));
    }

    @Override
    public IotClient removeClient(Object clientKey) {
        return remove(this.getClientKey(clientKey));
    }

    @Override
    public List<IotClient> clients() {
        Collection<IotClient> values = this.getMapper().values();
        return new ArrayList<>(values);
    }

    @Override
    public ClientComponent getClientComponent() {
        return this.clientComponent;
    }

    @Override
    public void setClientComponent(ClientComponent component) {
        this.clientComponent = component;
    }

    protected String getClientKey(Object clientKey) {
        if(clientKey instanceof ConnectProperties) {
            // 使用每个配置对象的hash作为唯一标识
            return ((ConnectProperties) clientKey).getHost() + ":" + ((ConnectProperties) clientKey).getPort()+":"+clientKey.hashCode();
        } else if(clientKey instanceof String){
            return (String) clientKey;
        } else {
            throw new IllegalArgumentException("不支持的[clientKey] 必须是[ConnectProperties or String]类型");
        }
    }
}
