package com.dream.iot.test.modbus;

import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.modbus.Payload;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientCommonProtocol;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientComponent;
import com.dream.iot.modbus.consts.ModbusBitStatus;
import com.dream.iot.modbus.consts.ModbusCoilStatus;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.TestConst;
import com.dream.iot.utils.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class ModbusTestHandle implements ClientProtocolHandle<ModbusTcpClientCommonProtocol>, IotTestHandle {

    @Autowired
    private ModbusTcpClientComponent modbusTcpClientComponent;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(ModbusTcpClientCommonProtocol protocol) {
        return null;
    }

    @Override
    public void start() throws Exception {
        System.out.println("------------------ modbus客户端测试时使用的服务端模拟工具地址(https://gitee.com/qsaker/QtSwissArmyKnife) -------------");

        //测试读写单个线圈
        ModbusCoilStatus write05 = ModbusCoilStatus.ON;
        ModbusTcpClientCommonProtocol.buildWrite05(1, 0, ModbusCoilStatus.ON).request();
        ModbusTcpClientCommonProtocol.buildRead01(1, 0, 1).request(protocol -> {
            ModbusTcpClientCommonProtocol commonProtocol = (ModbusTcpClientCommonProtocol) protocol;
            ModbusBitStatus modbusBitStatus = commonProtocol.getPayload().readStatus(0);

            boolean status = (write05 == ModbusCoilStatus.ON && modbusBitStatus == ModbusBitStatus.ON)
                    || (write05 == ModbusCoilStatus.OFF && modbusBitStatus == ModbusBitStatus.OFF);

            ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
            logger.info(TestConst.LOGGER_MODBUS_DESC, modbusTcpClientComponent.getName()
                    , "Write05 -> Read01", head.getEquipCode(), head.getMessageId()
                    , status ? "通过" : "失败");
            return null;
        });

        // 测试读写多个线圈
        byte[] write0f = new byte[] {(byte) 0xFF};
        ModbusTcpClientCommonProtocol.buildWrite0F(1, 3, write0f).request();
        ModbusTcpClientCommonProtocol.buildRead01(1, 3, 16).request(protocol -> {
            ModbusTcpClientCommonProtocol commonProtocol = (ModbusTcpClientCommonProtocol) protocol;
            Payload payload = commonProtocol.getPayload();
            ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
            logger.info(TestConst.LOGGER_MODBUS_DESC, modbusTcpClientComponent.getName()
                    , "Write0F -> Read01", head.getEquipCode(), head.getMessageId()
                    , write0f[0] == payload.getPayload()[0] ? "通过" : "失败");
            return null;
        });

        // 写单个寄存器测试
        short write06 = 72;
        ModbusTcpClientCommonProtocol.buildWrite06(1, 3, ByteUtil.getBytesOfReverse(write06)).request();
        ModbusTcpClientCommonProtocol.buildRead03(1, 3, 1).request(protocol -> {
            ModbusTcpClientCommonProtocol commonProtocol = (ModbusTcpClientCommonProtocol) protocol;
            ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
            short i = commonProtocol.getPayload().readShort(3);
            logger.info(TestConst.LOGGER_MODBUS_DESC, modbusTcpClientComponent.getName()
                    , "Write06 -> Read03", head.getEquipCode(), head.getMessageId()
                    , i == write06 ? "通过" : "失败");
            return null;
        });

        ModbusTcpClientCommonProtocol.buildWrite06(1, 4, write06).request();
        ModbusTcpClientCommonProtocol.buildRead03(1, 4, 1).request(protocol -> {
            ModbusTcpClientCommonProtocol commonProtocol = (ModbusTcpClientCommonProtocol) protocol;
            ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
            short i = commonProtocol.getPayload().readShort(4);
            logger.info(TestConst.LOGGER_MODBUS_DESC, modbusTcpClientComponent.getName()
                    , "Write06(arg) -> Read03", head.getEquipCode(), head.getMessageId()
                    , i == write06 ? "通过" : "失败");
            return null;
        });

        // 写多个寄存器测试
        int write10 = 1000000;
        ModbusTcpClientCommonProtocol.buildWrite10(1, 6, 2, ByteUtil.getBytes(write10)).request();
        ModbusTcpClientCommonProtocol.buildRead03(1, 6, 2).request(protocol -> {
            ModbusTcpClientCommonProtocol commonProtocol = (ModbusTcpClientCommonProtocol) protocol;
            ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
            int i = commonProtocol.getPayload().readInt(6);
            logger.info(TestConst.LOGGER_MODBUS_DESC, modbusTcpClientComponent.getName()
                    , "Write10 -> Read03", head.getEquipCode(), head.getMessageId()
                    , i == write10 ? "通过" : "失败");
            return null;
        });

        /**
         *
         */
        ModbusTcpClientCommonProtocol.buildWrite10(1, 660, (short)5, 2, 1.8f, 3.5, 300000l, "你好").request();
        ModbusTcpClientCommonProtocol.buildRead03(1, 660, 16).request(protocol -> {
            ModbusTcpClientCommonProtocol commonProtocol = (ModbusTcpClientCommonProtocol) protocol;
            short aShort = commonProtocol.getPayload().readShort(660);
            int readInt = commonProtocol.getPayload().readInt(661);
            float readFloat = commonProtocol.getPayload().readFloat(663);
            double readDouble = commonProtocol.getPayload().readDouble(665);
            long readLong = commonProtocol.getPayload().readLong(669);
            String readString = commonProtocol.getPayload().readString(673, 3);

            ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
            logger.info(TestConst.LOGGER_MODBUS_DESC, modbusTcpClientComponent.getName(), "Write10(args) -> Read03", head.getEquipCode(), head.getMessageId()
                    , 5==aShort && 2==readInt && 1.8f==readFloat && 3.5==readDouble && 300000l == readLong && "你好".equals(readString) ? "通过" : "失败");

            return null;
        });

        TimeUnit.SECONDS.sleep(3);
    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
