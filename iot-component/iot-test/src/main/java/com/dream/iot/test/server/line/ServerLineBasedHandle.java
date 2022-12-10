package com.dream.iot.test.server.line;

import com.dream.iot.consts.ExecStatus;
import com.dream.iot.server.ServerProtocolHandle;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.message.line.LineMessage;
import com.dream.iot.test.message.line.LineMessageHead;
import com.dream.iot.test.server.line.protocol.LineClientInitiativeProtocol;
import com.dream.iot.test.server.line.protocol.LineServerInitiativeProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ServerLineBasedHandle implements ServerProtocolHandle<LineClientInitiativeProtocol> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired(required = false)
    private TestLineBasedFrameDecoderComponent component;

    @Override
    public Object handle(LineClientInitiativeProtocol initiativeProtocol) {
        LineMessage lineMessage = initiativeProtocol.requestMessage();
        LineMessageHead head = lineMessage.getHead();

        new LineServerInitiativeProtocol(head.getEquipCode()).request(protocol -> {
            LineMessageHead request = protocol.requestMessage().getHead();

            String messageId = request.getMessageId();
            if(protocol.getExecStatus() == ExecStatus.success) {
                logger.info(TestConst.LOGGER_PROTOCOL_DESC, component.getName(), "服务端测试",
                        head.getEquipCode(), messageId, "通过");
            } else {
                logger.error(TestConst.LOGGER_PROTOCOL_DESC, component.getName(), "服务端测试",
                        head.getEquipCode(), messageId, "失败("+protocol.getExecStatus().desc+")");
            }

            return null;
        });

        return null;
    }
}
