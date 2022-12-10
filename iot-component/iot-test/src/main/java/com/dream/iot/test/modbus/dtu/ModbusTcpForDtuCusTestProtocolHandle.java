package com.dream.iot.test.modbus.dtu;

import com.dream.iot.consts.ExecStatus;
import com.dream.iot.modbus.server.dtu.ModbusTcpForDtuServerComponent;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.server.ServerProtocolHandle;
import com.dream.iot.test.TestConst;
import com.dream.iot.utils.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModbusTcpForDtuCusTestProtocolHandle implements ServerProtocolHandle<ModbusTcpForDtuCusTestProtocol> {

    @Autowired(required = false)
    private ModbusTcpForDtuServerComponent component;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(ModbusTcpForDtuCusTestProtocol protocol) {
        ModbusTcpHeader head = protocol.responseMessage().getHead();
        ModbusTcpBody body = protocol.responseMessage().getBody();
        byte[] content = body.getContent();
        if (protocol.getExecStatus() == ExecStatus.success) {
            short toShort = ByteUtil.bytesToShortOfReverse(content, 0);
            logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName()
                    , "自定义协议读Read03",head.getEquipCode(), head.getMessageId()
                    , toShort == protocol.getWriteValue().shortValue() ? "通过" : "失败");
        } else {
            logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                    , "自定义协议读Read03", head.getEquipCode(), head.getMessageId(), "失败");
        }
        return null;
    }
}
