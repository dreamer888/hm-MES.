package com.dream.iot.test;

import org.springframework.core.Ordered;

public interface IotTestHandle extends Ordered {

    void start() throws Exception;
}
