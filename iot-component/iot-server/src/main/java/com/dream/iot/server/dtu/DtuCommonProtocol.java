package com.dream.iot.server.dtu;

import com.dream.iot.Protocol;
import com.dream.iot.server.dtu.message.DtuMessage;

public interface DtuCommonProtocol<M extends DtuMessage> extends Protocol {

    @Override
    M requestMessage();

    @Override
    M responseMessage();
}
