package com.dream.iot.test.client.line;

import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.ServerInfoUtil;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.message.line.LineMessageHead;
import com.dream.iot.utils.UniqueIdGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.time.Instant;

public class LineClientHandle implements IotTestHandle, ClientProtocolHandle<LineClientRequestProtocol> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    @Autowired
    private LineClientComponent clientComponent;

    @Override
    public Object handle(LineClientRequestProtocol protocol) {
        String desc = "客户端测试";
        LineMessageHead messageHead = protocol.requestMessage().getHead();
        String equipCode = messageHead.getEquipCode();

        if(protocol.getExecStatus() == ExecStatus.success) {
            LineMessageHead head = protocol.responseMessage().getHead();
            logger.info(TestConst.LOGGER_PROTOCOL_DESC, clientComponent.getName()
                    , desc, equipCode, messageHead.getMessageId()
                    , ServerInfoUtil.compare(head.getPayload(), messageHead.getPayload()) ? "通过" : "不通过");
        } else {
            logger.warn(TestConst.LOGGER_PROTOCOL_DESC, clientComponent.getName()
                    , desc, equipCode, messageHead.getMessageId(), "失败(" + protocol.getExecStatus().desc + ")");
        }

        return null;
    }

    @Override
    public void start() {
        String deviceSn = "LTD:"+ UniqueIdGen.deviceSn();
        scheduler.scheduleAtFixedRate(() -> {
            new LineClientRequestProtocol(deviceSn).request();
        }, Instant.now().plusSeconds(10), Duration.ofSeconds(5));
    }

    @Override
    public int getOrder() {
        return 10000 * 50;
    }
}
