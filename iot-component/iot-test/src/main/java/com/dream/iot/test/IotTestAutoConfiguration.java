package com.dream.iot.test;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.mqtt.IotMqttAutoConfiguration;
import com.dream.iot.client.proxy.ProxyClientComponent;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientComponent;
import com.dream.iot.modbus.server.dtu.ModbusRtuForDtuServerComponent;
import com.dream.iot.modbus.server.dtu.ModbusTcpForDtuServerComponent;
import com.dream.iot.plc.omron.OmronComponent;
import com.dream.iot.plc.omron.OmronUdpComponent;
import com.dream.iot.plc.siemens.SiemensS7Component;
import com.dream.iot.redis.IotRedisConfiguration;
import com.dream.iot.server.ServerBootstrapInitializing;
import com.dream.iot.server.dtu.DtuMessageType;
import com.dream.iot.taos.IotTaosAutoConfiguration;
import com.dream.iot.test.client.breaker.BreakerClientComponent;
import com.dream.iot.test.client.breaker.BreakerDataHandle;
import com.dream.iot.test.client.fixed.FixedLengthClientComponent;
import com.dream.iot.test.client.fixed.FixedLengthRequestHandle;
import com.dream.iot.test.client.line.LineClientComponent;
import com.dream.iot.test.client.line.LineClientHandle;
import com.dream.iot.test.client.mutual.MutualClientTestComponent;
import com.dream.iot.test.mes.NettyUDPServer;
import com.dream.iot.test.listener.AsyncServerTestListener;
import com.dream.iot.test.listener.ClientTestListener;
import com.dream.iot.test.modbus.ModbusTestHandle;
import com.dream.iot.test.modbus.dtu.ModbusDtuTestHandle;
import com.dream.iot.test.modbus.dtu.ModbusRtuForDtuClientTestComponent;
import com.dream.iot.test.modbus.dtu.ModbusTcpForDtuClientTestComponent;
import com.dream.iot.test.mqtt.MqttClientTestComponent;
import com.dream.iot.test.mqtt.MqttClientTestHandle;
import com.dream.iot.test.plc.omron.OmronTcpTestHandle;
import com.dream.iot.test.plc.omron.OmronUdpTestHandle;
import com.dream.iot.test.plc.siemens.SiemensS7TestHandle;
import com.dream.iot.test.proxy.ProxyClientCase;
import com.dream.iot.test.server.breaker.BreakerServerComponent;
import com.dream.iot.test.server.fixed.FixedLengthClientRequestHandle;
import com.dream.iot.test.server.fixed.TestFixedLengthDecoderComponent;
import com.dream.iot.test.server.line.ServerLineBasedHandle;
import com.dream.iot.test.server.line.TestLineBasedFrameDecoderComponent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * create time: 2021/9/4
 *
 * @author dream
 * @since 1.0
 */
@EnableAsync
@AutoConfigureBefore({IotMqttAutoConfiguration.class, IotRedisConfiguration.class, IotTaosAutoConfiguration.class})
@EnableConfigurationProperties(IotTestProperties.class)
public class IotTestAutoConfiguration {

    @Autowired
    private IotTestProperties properties;


    //TestOmronUdpConnectProperties

    /**
     * tcp系统及option调试测试
     * @return
     */
    @Bean
    public ServerBootstrapInitializing testServerInitializing() {
        return new TestServerInitializing();
    }

    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.mutual.start:false}")
    public MutualClientTestComponent simpleClientTestComponent() {
        return new MutualClientTestComponent(properties.getMutual());
    }

    @Bean
    @ConditionalOnExpression("${iot.test.line.start:false} and ${iot.test.server:false}")
    public TestLineBasedFrameDecoderComponent lineBasedFrameDecoderComponent() {
        ConnectProperties connectProperties = new ConnectProperties();
        BeanUtils.copyProperties(properties.getLine(), connectProperties, "host");
        return new TestLineBasedFrameDecoderComponent(connectProperties);
    }

    @Bean
    @ConditionalOnBean(TestLineBasedFrameDecoderComponent.class)
    public ServerLineBasedHandle serverLineBasedHandle() {
        return new ServerLineBasedHandle();
    }

    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.line.start:false}")
    public LineClientComponent lineClientComponent() {
        return new LineClientComponent(properties.getLine());
    }

    @Bean
    @ConditionalOnBean(LineClientComponent.class)
    public LineClientHandle lineClientHandle() {
        return new LineClientHandle();
    }

    /**
     * 固定长度解码器服务端测试
     * @return
     */
    @Bean
    @ConditionalOnExpression("${iot.test.fixed.start:false} and ${iot.test.server:false}")
    public TestFixedLengthDecoderComponent fixedLengthDecoderComponent() {
        ConnectProperties connectProperties = new ConnectProperties();
        BeanUtils.copyProperties(properties.getFixed(), connectProperties, "host");
        return new TestFixedLengthDecoderComponent(connectProperties);
    }

    @Bean
    @ConditionalOnBean(TestFixedLengthDecoderComponent.class)
    public FixedLengthClientRequestHandle fixedLengthClientRequestHandle() {
        return new FixedLengthClientRequestHandle();
    }

    /**
     * 固定长度解码器客户端测试
     * @return
     */
    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.fixed.start:false}")
    public FixedLengthClientComponent fixedLengthClientComponent() {
        return new FixedLengthClientComponent(properties.getFixed());
    }

    @Bean
    @ConditionalOnBean(FixedLengthClientComponent.class)
    public FixedLengthRequestHandle fixedLengthRequestHandle() {
        return new FixedLengthRequestHandle();
    }

    /**
     * 自定义的mqtt协议客户端测试组件
     * @return
     */
    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.mqtt.start:false}")
    public MqttClientTestComponent mqttClientTestComponent() {
        return new MqttClientTestComponent();
    }

    @Bean
    @ConditionalOnBean(MqttClientTestComponent.class)
    public MqttClientTestHandle mqttClientTestHandle() {
        return new MqttClientTestHandle();
    }

    /**
     * 西门子S7-200组件
     * @return
     */
    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.siemens.start:false}")
    public SiemensS7Component siemensS7Component() {
        return new SiemensS7Component(properties.getSiemens());
    }

    @Bean
    @ConditionalOnBean(SiemensS7Component.class)
    public SiemensS7TestHandle siemensS7TestHandle() {
        return new SiemensS7TestHandle();
    }

   /* @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.omronUdp.start:false}")
    public OmronUdpComponent omronUdpComponent() {
        return new OmronUdpComponent(properties.getOmronUdp());
    }
*/

   @Bean
   @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.omron.start:false}")
    public OmronComponent omronComponent() {
        return new OmronComponent(properties.getOmron());
    }


    @Bean
    @ConditionalOnBean(OmronComponent.class)
    public OmronTcpTestHandle omronTcpTestHandle() {
        return new OmronTcpTestHandle();

        ////OmronTcpTestHandle  里面访问数据库是ok的
    }



    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.omronUdp.start:false}")
    public OmronUdpComponent OmronUdpComponent() {
        return new OmronUdpComponent(properties.getOmronUdp());
    }

    @Bean
    @ConditionalOnBean(OmronUdpComponent.class)
    public OmronUdpTestHandle omronUdpTestHandle() {
        return new OmronUdpTestHandle();
    }

    @Bean
    @ConditionalOnExpression("${iot.test.nettyUdp:true} ")
    public NettyUDPServer UdpTestHandle() throws Exception {
        return new  NettyUDPServer();
    }



    @Bean
    @ConditionalOnBean(ProxyClientComponent.class)
    public ProxyClientCase proxyClientCase() {
        return new ProxyClientCase();
    }

    /*********************************************  Modbus Tcp And DTU  *******************************/
    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.modbus.start:false}")
    public ModbusTcpClientComponent modbusTcpClientComponent() {
        return new ModbusTcpClientComponent(properties.getModbus());
    }

    @Bean
    @ConditionalOnBean(ModbusTcpClientComponent.class)
    public ModbusTestHandle modbusTestHandle() {
        return new ModbusTestHandle();
    }

    // Dtu For Server(Modbus Tcp)
    @Bean
    @ConditionalOnExpression("${iot.test.modbus-tcp-dtu.start:false} and ${iot.test.server:false}")
    public ModbusTcpForDtuServerComponent modbusTcpDtuDecodeComponent() {
        return new ModbusTcpForDtuServerComponent(properties.getModbusTcpDtu());
    }

    // Dtu设备模拟组件(Modbus Tcp)
    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.modbus-tcp-dtu.start:false}")
    public ModbusTcpForDtuClientTestComponent tcpForDtuModbusClientTestComponent() {
        String host = properties.getServerHost();
        Integer port = properties.getModbusTcpDtu().getPort();
        return new ModbusTcpForDtuClientTestComponent(new ClientConnectProperties(host, port));
    }

    // Dtu For Server(Modbus Rtu)
    @Bean
    @ConditionalOnExpression("${iot.test.modbus-rtu-dtu.start:false} and ${iot.test.server:false}")
    public ModbusRtuForDtuServerComponent modbusRtuForDtuComponent() {
        return new ModbusRtuForDtuServerComponent(properties.getModbusRtuDtu(), DtuMessageType.HEX);
    }

    // Dtu设备模拟组件(Modbus Rtu)
    @Bean
    @ConditionalOnExpression("${iot.test.client:false} and ${iot.test.modbus-rtu-dtu.start:false}")
    public ModbusRtuForDtuClientTestComponent rtuForDtuModbusClientTestComponent() {
        String host = properties.getServerHost();
        Integer port = properties.getModbusRtuDtu().getPort();
        return new ModbusRtuForDtuClientTestComponent(new ClientConnectProperties(host, port));
    }

    @Bean
    @ConditionalOnExpression("${iot.test.modbus-rtu-dtu.start:false} or ${iot.test.modbus-tcp-dtu.start:false}")
    public ModbusDtuTestHandle modbusDtuTestHandle() {
        return new ModbusDtuTestHandle();
    }

    /**
     * ###################################### 断路器数据上报采集、redis、taos数据存储测试 ###################################
     */
    @Bean
    @ConditionalOnExpression("${iot.test.breaker.start:false} and ${iot.test.server:false}")
    public BreakerServerComponent breakerServerComponent() {
        IotTestProperties.TestConnectConfig breaker = properties.getBreaker();
        return new BreakerServerComponent(new ConnectProperties(breaker.getPort()));
    }

    @Bean
    @ConditionalOnExpression("${iot.test.breaker.start:false} and ${iot.test.client:false}")
    public BreakerClientComponent breakerClientComponent() {
        return new BreakerClientComponent(properties.getBreaker());
    }

    @Bean
    @ConditionalOnExpression("${iot.test.breaker.start:false} and ${iot.test.client:false}")
    public BreakerDataHandle breakerDataHandle() {
        return new BreakerDataHandle(properties.getBreaker());
    }

    @Bean
    @ConditionalOnExpression("${iot.test.start-listener:false}")
    public ClientTestListener clientTestListener() {
        return new ClientTestListener();
    }

    @Bean
    @ConditionalOnExpression("${iot.test.start-listener:false}")
    public AsyncServerTestListener asyncServerTestListener() {
        return new AsyncServerTestListener();
    }
}
