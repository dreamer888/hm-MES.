package com.dream.iot.client;

import com.dream.iot.IotThreadManager;
import com.dream.iot.client.component.SingleTcpClientComponent;
import com.dream.iot.client.component.SocketClientComponent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientComponentFactory implements InitializingBean {

    private IotThreadManager threadManager;
    private List<ClientComponent> clientComponents = new ArrayList<>();
    private Map<Class<? extends ClientMessage>, ClientComponent> messageComponentMap = new HashMap(8);

    public ClientComponentFactory(IotThreadManager threadManager, List<ClientComponent> clientComponents) {
        this.threadManager = threadManager;

        if(!CollectionUtils.isEmpty(clientComponents)) {
            this.clientComponents = clientComponents;
        }
    }

    public List<ClientComponent> getComponents() {
        return this.clientComponents;
    }

    public ClientComponent getByClass(Class<? extends ClientMessage> messageClass) {
        return messageComponentMap.get(messageClass);
    }

    public ClientComponent getUdpClientComponent() {

        for (ClientComponent value : messageComponentMap.values()) {

            if(value.getConfig().toString().contains("9600"))
                    return value;
        }

        return null;

    }



    @Override
    public void afterPropertiesSet() throws Exception {
        if(!CollectionUtils.isEmpty(clientComponents)) {
            for(int i=0; i<clientComponents.size(); i++) {
                ClientComponent clientComponent = clientComponents.get(i);
                if(clientComponent instanceof SocketClientComponent) {
                    clientComponent.init(threadManager.getWorkerGroup());
                } else if(clientComponent instanceof SingleTcpClientComponent) {
                    clientComponent.init(threadManager.getWorkerGroup());
                } else {
                    clientComponent.init();
                }

                messageComponentMap.put(clientComponent.getMessageClass(), clientComponent);
            }
        }
    }

}
