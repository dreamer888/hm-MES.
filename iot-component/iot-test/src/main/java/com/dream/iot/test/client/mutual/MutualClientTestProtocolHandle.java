package com.dream.iot.test.client.mutual;

import com.dream.iot.client.ClientProtocolHandle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "iot.test", name = "mutual.start", havingValue = "true")
public class MutualClientTestProtocolHandle implements ClientProtocolHandle<MutualClientTestProtocol> {

    @Override
    public Object handle(MutualClientTestProtocol protocol) {

        /**
         * 在接受到模拟服务端发送的报文后, 在把报文发送给他
         */
        new MutualClientInitTestProtocol(protocol.getEquipCode(), protocol.getReadMsg()).request();

        return null;
    }
}
