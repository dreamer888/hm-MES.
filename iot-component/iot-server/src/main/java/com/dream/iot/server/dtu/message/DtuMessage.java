package com.dream.iot.server.dtu.message;

import com.dream.iot.Message;
import com.dream.iot.ProtocolType;

public interface DtuMessage extends Message {

    String getEquipCode();

    void setEquipCode(String equipCode);

    ProtocolType getProtocolType();

    DtuMessage setProtocolType(ProtocolType type);

    /**
     * 构建Dtu设备的第一包设备编号报文头
     * @return
     */
    MessageHead buildFirstHead();

}
