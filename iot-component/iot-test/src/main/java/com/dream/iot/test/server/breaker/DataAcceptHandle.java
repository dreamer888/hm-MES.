package com.dream.iot.test.server.breaker;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.client.mqtt.gateway.MqttGatewayConnectProperties;
import com.dream.iot.client.mqtt.gateway.MqttGatewayHandle;
import com.dream.iot.client.mqtt.gateway.adapter.MqttGatewayJsonHandle;
import com.dream.iot.handle.proxy.ProtocolHandleProxy;
import com.dream.iot.redis.handle.RedisListHandle;
import com.dream.iot.redis.producer.RedisProducer;
import com.dream.iot.server.ServerProtocolHandle;
import com.dream.iot.taos.TaosHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.taos.TaosBreakerDataTable;
import com.dream.iot.test.taos.TaosBreakerUsingStable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@ConditionalOnExpression("${iot.test.breaker.start:false} and ${iot.test.server:false}")
public class DataAcceptHandle implements ServerProtocolHandle<DataAcceptProtocol>, InitializingBean
        , TaosHandle<DataAcceptProtocol>, RedisListHandle<DataAcceptProtocol, Object>, MqttGatewayJsonHandle<DataAcceptProtocol, Object> {

    @Autowired
    private IotTestProperties properties;
    private MqttGatewayConnectProperties gatewayConnectProperties;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean filter(Object entity, Class<? extends ProtocolHandleProxy> proxy) {
        if(TaosHandle.class == proxy && properties.isTaosStart()) {
            return true;
        } else if(MqttGatewayHandle.class == proxy && properties.getMqtt().isStart()) {
            return true;
        } else if(RedisProducer.class == proxy && properties.isRedisStart()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer consumer(List<Object> objects) {
        logger.info("Redis消费测试 消费数量：{}", objects.size());
        return objects.size();
    }

    @Override
    public Object handle(DataAcceptProtocol protocol) {
        final int i = RandomUtil.randomInt(1, 9);
        if(i % 2 == 0) { // 测试自动创建数据表
            TaosBreakerUsingStable entity = new TaosBreakerUsingStable(protocol.getEquipCode());
            entity.setI(protocol.getI());
            entity.setV(protocol.getV());
            entity.setPy(protocol.getPy());
            entity.setSn(protocol.getEquipCode());
            entity.setPower1(protocol.getPower1());
            entity.setPower2(protocol.getPower2());

            return entity;
        } else { // 测试插入数据表
            TaosBreakerDataTable dataTable = new TaosBreakerDataTable();
            dataTable.setI(protocol.getI());
            dataTable.setV(protocol.getV());
            dataTable.setPy(protocol.getPy());
            dataTable.setTs(new Date());
            dataTable.setSn(protocol.getEquipCode());
            dataTable.setPower1(protocol.getPower1());
            dataTable.setPower2(protocol.getPower2());
            return dataTable;
        }
    }

    @Override
    public String getKey() {
        return "Break_Redis_test";
    }

    @Override
    public MqttGatewayConnectProperties getProperties(Object entity) {
        return this.gatewayConnectProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IotTestProperties.TestMqttConnectProperties mqtt = properties.getMqtt();
        if(mqtt != null) {
            this.gatewayConnectProperties = new MqttGatewayConnectProperties(mqtt.getHost()
                    , mqtt.getPort(), "MqttGatewayTestClientId", "/breaker/gateway");
        }
    }
}
