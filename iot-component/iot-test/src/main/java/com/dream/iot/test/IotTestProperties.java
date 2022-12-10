package com.dream.iot.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.mqtt.MqttConnectProperties;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.plc.omron.OmronConnectProperties;
import com.dream.iot.plc.omron.OmronUdpConnectProperties;
import com.dream.iot.plc.siemens.SiemensConnectProperties;
import com.dream.iot.plc.siemens.SiemensModel;
import com.dream.iot.test.client.mutual.MutualType;
import com.dream.iot.test.db.config.entity.SpConfig;
import com.dream.iot.test.db.config.service.ISpConfigService;
import com.dream.iot.test.db.config.service.impl.SpConfigServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "iot.test")
public class IotTestProperties {

    /**
     * 启用客户端测试
     */
    private boolean client;

    /**
     * 启用服务端测试
     */
    private boolean server;

    /**
     * 服务端主机
     */
    private String serverHost;

    /**
     * 是否启用Taos测试
     */
    private boolean taosStart;

    /**
     * 是否启用redis测试
     */
    private boolean redisStart;

    /**
     * 开启Dtu的At测试
     */
    private boolean dtuAtStart;

    /**
     * 启用事件监听测试
     */
    private boolean startListener;

    /**
     * 基于换行符解码组件测试
     */
    private TestConnectConfig line;

    /**
     * 基于固定长度解码器测试
     */
    private TestMultiConnectConfig fixed;

    /**
     * 交互测试
     */
    private MutualConnectProperties mutual;

    /**
     * mqtt协议测试
     */
    private TestMqttConnectProperties mqtt;

    /**
     * Modbus Tcp 协议测试
     */
    private TestConnectConfig modbus;

    /**
     * modbus tcp Of Dtu 测试
     */
    private ModbusTcpDtuConnectProperties modbusTcpDtu;

    /**
     * modbus tcp Of Rtu 测试
     */
    private ModbusTcpDtuConnectProperties modbusRtuDtu;

    /**
     * 西门子PLC 协议测试
     */
    private TestSiemensConnectProperties siemens;

    /**
     * 欧姆龙PLC 协议测试
     */
    private TestOmronConnectProperties omron;

    private TestOmronUdpConnectProperties omronUdp;

    /**
     * 断路器
     */
    private BreakerConnectConfig breaker;

    /**
     * udp测试
     */
    private TestUdpConnectConfig udp;

    /**
     * websocket测试配置
     */
    private TestWebsocketConnectConfig websocket;

    /**
     * 服务端主机地址
     */
    private String host = "127.0.0.1";

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public TestConnectConfig getLine() {
        return line;
    }

    public void setLine(TestConnectConfig line) {
        this.line = line;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static class TestUdpConnectConfig extends ConnectProperties {

        private boolean start;

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }

    public static class TestWebsocketConnectConfig extends ConnectProperties {

        private boolean start;

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }

    public static class TestConnectConfig extends ClientConnectProperties {

        private boolean start;

        public TestConnectConfig() { }

        public TestConnectConfig(Integer port) {
            super(port);
        }

        public TestConnectConfig(String host, Integer port) {
            super(host, port);
        }

        /**
         * 测试的客户端数量
         */
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }

    public static class TestMultiConnectConfig extends TestConnectConfig {

        private String deviceSn;

        public TestMultiConnectConfig() {
            this.deviceSn = ClientSnGen.getClientSn() + "";
        }

        public TestMultiConnectConfig(String prefix) {
            this.deviceSn = ClientSnGen.getClientSn(prefix);
        }

        public String getDeviceSn() {
            return deviceSn;
        }

        public void setDeviceSn(String deviceSn) {
            this.deviceSn = deviceSn;
        }

        @Override
        public String connectKey() {
            return super.connectKey() + ":" +this.deviceSn;
        }
    }

    public static class BreakerConnectConfig extends TestMultiConnectConfig {

        private String deviceSn;

        /**
         * 每个客户端发送的报文数量
         */
        private int countOfPeer = 500;

        public BreakerConnectConfig() {
            this.deviceSn = ClientSnGen.getClientSn() + "";
        }

        public int getCountOfPeer() {
            return countOfPeer;
        }

        public void setCountOfPeer(int countOfPeer) {
            this.countOfPeer = countOfPeer;
        }
    }


    public static class MutualConnectProperties extends TestConnectConfig {

        private MutualType type = MutualType.UTF8;

        public MutualType getType() {
            return type;
        }

        public void setType(MutualType type) {
            this.type = type;
        }
    }

    public static class  ModbusTcpDtuConnectProperties extends ConnectProperties {
        /**
         * 是否启用测试
         */
        private boolean start;

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }

    public static class TestMqttConnectProperties extends MqttConnectProperties {

        private boolean start;

        private String deviceSn;

        public TestMqttConnectProperties() {
            super(ClientSnGen.getClientSn("MQTT::"));
            this.deviceSn = getClientId();
        }

        public String getDeviceSn() {
            return deviceSn;
        }

        public void setDeviceSn(String deviceSn) {
            this.deviceSn = deviceSn;
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }

    public static class TestSiemensConnectProperties extends SiemensConnectProperties {

        /**
         * 是否启用测试
         */
        private boolean start;

        public TestSiemensConnectProperties() {
            super("127.0.0.1", SiemensModel.S1200);
        }

        public TestSiemensConnectProperties(String host, SiemensModel model) {
            super(host, model);
        }

        public TestSiemensConnectProperties(String host, Integer port, SiemensModel model) {
            super(host, port, model);
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }

    public static class TestOmronConnectProperties extends OmronConnectProperties {

        @Autowired
        private ISpConfigService iSpConfigService;

        @Autowired
       public  static SpConfigServiceImpl spConfigServiceImpl;


        private boolean start;

        private static SpConfig spConfig;

        public static SpConfig GetCurrentConfig()
        {
            if (spConfig !=null)
                return spConfig;

            QueryWrapper<SpConfig> queryWrapper = new QueryWrapper();
            queryWrapper.like("lineId", "1336868662673440");   //磁阻变压器

            if(spConfigServiceImpl==null)
                spConfigServiceImpl  =  new  SpConfigServiceImpl();
            //List<SpConfig> list = spConfigServiceImpl.list(queryWrapper);
            List<SpConfig> list = spConfigServiceImpl.list();
            //SpConfig spConfig = iSpConfigService.getById(list.get(0).getId());
            SpConfig spConfig = list.get(0);
             spConfig = list.get(0);
            return  spConfig;
        }

        public TestOmronConnectProperties() {




            //super("127.0.0.1", 9600);
            //super("192.168.250.50", 9600);
            //super(spConfig.getServerIp(), 9600);
          // this.setHost(GetCurrentConfig().getServerIp());
            //this.setPort( GetCurrentConfig().getServerPort());
            int i = 1;
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }



    //@Component
    public static class TestOmronUdpConnectProperties extends OmronUdpConnectProperties {


        private boolean start;


        public TestOmronUdpConnectProperties() {

            //super("127.0.0.1", 9600);
            //super("192.168.250.50", 9600);
            //super("127.0.0.1",9600,"127.0.0.1",9601);
            //super(spConfig.getServerIp(), 9600);
            // this.setHost(GetCurrentConfig().getServerIp());
            //this.setPort( GetCurrentConfig().getServerPort());
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }



    public TestMultiConnectConfig getFixed() {
        return fixed;
    }

    public void setFixed(TestMultiConnectConfig fixed) {
        this.fixed = fixed;
    }

    public TestMqttConnectProperties getMqtt() {
        return mqtt;
    }

    public void setMqtt(TestMqttConnectProperties mqtt) {
        this.mqtt = mqtt;
    }

    public TestConnectConfig getModbus() {
        return modbus;
    }

    public void setModbus(TestConnectConfig modbus) {
        this.modbus = modbus;
    }

    public TestSiemensConnectProperties getSiemens() {
        return siemens;
    }

    public void setSiemens(TestSiemensConnectProperties siemens) {
        this.siemens = siemens;
    }

    public TestOmronConnectProperties getOmron() {
        return omron;
    }

    public void setOmron(TestOmronConnectProperties omron) {
        this.omron = omron;
    }

//lgl
    public TestOmronUdpConnectProperties getOmronUdp() {
        return omronUdp;
    }
    public void setOmronUdp(TestOmronUdpConnectProperties omronUdp) {
        this.omronUdp = omronUdp;
    }

    public ModbusTcpDtuConnectProperties getModbusTcpDtu() {
        return modbusTcpDtu;
    }

    public void setModbusTcpDtu(ModbusTcpDtuConnectProperties modbusTcpDtu) {
        this.modbusTcpDtu = modbusTcpDtu;
    }

    public MutualConnectProperties getMutual() {
        return mutual;
    }

    public void setMutual(MutualConnectProperties mutual) {
        this.mutual = mutual;
    }

    public boolean isTaosStart() {
        return taosStart;
    }

    public void setTaosStart(boolean taosStart) {
        this.taosStart = taosStart;
    }

    public ModbusTcpDtuConnectProperties getModbusRtuDtu() {
        return modbusRtuDtu;
    }

    public void setModbusRtuDtu(ModbusTcpDtuConnectProperties modbusRtuDtu) {
        this.modbusRtuDtu = modbusRtuDtu;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public BreakerConnectConfig getBreaker() {
        return breaker;
    }

    public void setBreaker(BreakerConnectConfig breaker) {
        this.breaker = breaker;
    }

    public boolean isDtuAtStart() {
        return dtuAtStart;
    }

    public void setDtuAtStart(boolean dtuAtStart) {
        this.dtuAtStart = dtuAtStart;
    }

    public boolean isStartListener() {
        return startListener;
    }

    public void setStartListener(boolean startListener) {
        this.startListener = startListener;
    }

    public boolean isRedisStart() {
        return redisStart;
    }

    public void setRedisStart(boolean redisStart) {
        this.redisStart = redisStart;
    }

    public TestUdpConnectConfig getUdp() {
        return udp;
    }

    public void setUdp(TestUdpConnectConfig udp) {
        this.udp = udp;
    }

    public TestWebsocketConnectConfig getWebsocket() {
        return websocket;
    }

    public void setWebsocket(TestWebsocketConnectConfig websocket) {
        this.websocket = websocket;
    }
}
