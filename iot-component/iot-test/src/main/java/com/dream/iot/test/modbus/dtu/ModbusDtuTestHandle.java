package com.dream.iot.test.modbus.dtu;

import com.dream.iot.Message;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.modbus.ModbusCommonProtocol;
import com.dream.iot.modbus.Payload;
import com.dream.iot.modbus.consts.ModbusBitStatus;
import com.dream.iot.modbus.consts.ModbusCoilStatus;
import com.dream.iot.modbus.server.dtu.ModbusRtuForDtuCommonProtocol;
import com.dream.iot.modbus.server.dtu.ModbusRtuForDtuServerComponent;
import com.dream.iot.modbus.server.dtu.ModbusTcpForDtuCommonProtocol;
import com.dream.iot.modbus.server.dtu.ModbusTcpForDtuServerComponent;
import com.dream.iot.modbus.server.rtu.ModbusRtuHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.server.ServerProtocolHandle;
import com.dream.iot.server.TcpServerComponent;
import com.dream.iot.server.dtu.DtuCommonProtocolType;
import com.dream.iot.server.dtu.DtuDeviceSnProtocol;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.plc.TestPlcUtils;
import com.dream.iot.utils.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class ModbusDtuTestHandle implements ServerProtocolHandle<DtuDeviceSnProtocol>, IotTestHandle {

    private String modbusRtuDeviceSn;
    private String modbusTcpDeviceSn;

    @Autowired(required = false)
    private ModbusTcpForDtuServerComponent component;

    @Autowired(required = false)
    private ModbusRtuForDtuServerComponent rtuForDtuComponent;

    @Autowired
    private IotTestProperties properties;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(DtuDeviceSnProtocol deviceSnProtocol) {
        String equipCode = deviceSnProtocol.getEquipCode();
        // ascii格式
        if(equipCode.startsWith("TCP")) {
            this.modbusTcpDeviceSn = equipCode;
        }

        // 十六进制格式
        if(equipCode.startsWith("525455")) {
            this.modbusRtuDeviceSn = equipCode;
        }

        return null;
    }

    private void modbusWrite10BatchTest(ModbusCommonProtocol commonProtocol, ExecStatus status, int start, TcpServerComponent component) {
        Message.MessageHead head = commonProtocol.requestMessage().getHead();

        if (status == ExecStatus.success) {
            short aShort = commonProtocol.getPayload().readShort(start);
            int readInt = commonProtocol.getPayload().readInt(start + 1);
            float readFloat = commonProtocol.getPayload().readFloat(start + 3);
            double readDouble = commonProtocol.getPayload().readDouble(start + 5);
            long readLong = commonProtocol.getPayload().readLong(start + 9);
            String readString = commonProtocol.getPayload().readString(start + 13, 3);

            logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName(), "Write10(args) -> Read03", head.getEquipCode(), head.getMessageId()
                    , 5 == aShort && 2 == readInt && 1.8f == readFloat && 3.5 == readDouble && 300000l == readLong && "你好".equals(readString) ? "通过" : "失败");
        } else {
            logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                    , "Write10(args) -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
        }
    }

    @Override
    public void start() throws Exception {
        short write06 = TestPlcUtils.randomShorts(1)[0];
        int write10 = Integer.MAX_VALUE;
        byte[] write0f = new byte[] {(byte) 0xFF};
        ModbusCoilStatus write05 = ModbusCoilStatus.ON;

        if(this.modbusRtuDeviceSn != null) {
            String equipCode = this.modbusRtuDeviceSn;
            System.out.println("-------------------------------------- Modbus Rtu For Dtu 测试 ----------------------------------------------");
            long start = System.currentTimeMillis();
            if(properties.isDtuAtStart()) {
                System.out.println("发送AT指令(Modbus Rtu)：@DTU:0000:GSN");
                ModbusRtuForDtuCommonProtocol.build(equipCode, "@DTU:0000:GSN".getBytes(), DtuCommonProtocolType.AT).request(protocol -> {
                    if(protocol.getExecStatus() == ExecStatus.success) {
                        byte[] message = protocol.responseMessage().getMessage();
                        System.out.println("AT响应：" + new String(message));
                    } else {
                        System.out.println("AT响应失败：" + protocol.getExecStatus());
                    }
                    return null;
                });
            }

            //测试读写单个线圈
            ModbusRtuForDtuCommonProtocol.buildWrite05(equipCode, 1, 5, ModbusCoilStatus.ON).request();
            ModbusRtuForDtuCommonProtocol.buildRead01(equipCode, 1, 5, 1).request(protocol -> {
                if (protocol.getExecStatus() == ExecStatus.success) {
                    ModbusRtuForDtuCommonProtocol commonProtocol = (ModbusRtuForDtuCommonProtocol) protocol;
                    ModbusBitStatus modbusBitStatus = commonProtocol.getPayload().readStatus(0);

                    boolean status = (write05 == ModbusCoilStatus.ON && modbusBitStatus == ModbusBitStatus.ON)
                            || (write05 == ModbusCoilStatus.OFF && modbusBitStatus == ModbusBitStatus.OFF);

                    ModbusRtuHeader head = commonProtocol.requestMessage().getHead();
                    logger.info(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write05 -> Read01", head.getEquipCode(), head.getMessageId()
                            , status ? "通过" : "失败");
                } else {
                    ModbusRtuHeader head = protocol.requestMessage().getHead();
                    logger.error(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write05 -> Read01", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 测试读写多个线圈
            ModbusRtuForDtuCommonProtocol.buildWrite0F(equipCode, 1, 7, write0f).request();
            ModbusRtuForDtuCommonProtocol.buildRead01(equipCode, 1, 7, 8).request(protocol -> {
                ModbusRtuForDtuCommonProtocol commonProtocol = (ModbusRtuForDtuCommonProtocol) protocol;
                ModbusRtuHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    Payload payload = commonProtocol.getPayload();
                    logger.info(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write0F -> Read01", head.getEquipCode(), head.getMessageId()
                            , write0f[0] == payload.getPayload()[0] ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write0F -> Read01", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 写单个寄存器测试
            ModbusRtuForDtuCommonProtocol.buildWrite06(equipCode, 1, 8, ByteUtil.getBytesOfReverse(write06)).request();
            ModbusRtuForDtuCommonProtocol.buildRead03(equipCode, 1, 8, 1).request(protocol -> {
                ModbusRtuForDtuCommonProtocol commonProtocol = (ModbusRtuForDtuCommonProtocol) protocol;
                ModbusRtuHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    short i = commonProtocol.getPayload().readShort(8);
                    logger.info(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write06 -> Read03", head.getEquipCode(), head.getMessageId()
                            , i == write06 ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write06 -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            ModbusRtuForDtuCommonProtocol.buildWrite06(equipCode, 1, 9, write06).request();
            ModbusRtuForDtuCommonProtocol.buildRead03(equipCode, 1, 9, 1).request(protocol -> {
                ModbusRtuForDtuCommonProtocol commonProtocol = (ModbusRtuForDtuCommonProtocol) protocol;
                ModbusRtuHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    short i = commonProtocol.getPayload().readShort(9);
                    logger.info(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write06(arg) -> Read03", head.getEquipCode(), head.getMessageId()
                            , i == write06 ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write06(arg) -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 写多个寄存器测试
            ModbusRtuForDtuCommonProtocol.buildWrite10(equipCode, 1, 12, 2, ByteUtil.getBytes(write10)).request();
            ModbusRtuForDtuCommonProtocol.buildRead03(equipCode, 1, 12, 2).request(protocol -> {
                ModbusRtuForDtuCommonProtocol commonProtocol = (ModbusRtuForDtuCommonProtocol) protocol;
                ModbusRtuHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    int i = commonProtocol.getPayload().readInt(12);
                    logger.info(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write10 -> Read03", head.getEquipCode(), head.getMessageId()
                            , i == write10 ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, rtuForDtuComponent.getName()
                            , "Write10 -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            ModbusRtuForDtuCommonProtocol.buildWrite10(equipCode, 1, 860, (short)5, 2, 1.8f, 3.5, 300000l, "你好").request();
            ModbusRtuForDtuCommonProtocol.buildRead03(equipCode, 1, 860, 16).request(protocol -> {
                ModbusRtuForDtuCommonProtocol commonProtocol = (ModbusRtuForDtuCommonProtocol) protocol;
                modbusWrite10BatchTest(commonProtocol, protocol.getExecStatus(), 860, rtuForDtuComponent);
                return null;
            });

            long l = System.currentTimeMillis() - start;
            System.out.println("    总耗时：" + l + "(ms)   平均耗时：" + l / 12.0 + "(ms)");
            TimeUnit.SECONDS.sleep(1);
        }

        if(this.modbusTcpDeviceSn != null) {
            String equipCode = this.modbusTcpDeviceSn;
            System.out.println("-------------------------------------- Modbus Tcp For Dtu 测试 ----------------------------------------------");
            long start = System.currentTimeMillis();
            if(properties.isDtuAtStart()) {
                System.out.println("发送AT指令(Modbus Tcp)：@DTU:0000:ICCID");
                ModbusTcpForDtuCommonProtocol.build(equipCode, "@DTU:0000:ICCID".getBytes(), DtuCommonProtocolType.AT).request(protocol -> {
                    if(protocol.getExecStatus() == ExecStatus.success) {
                        byte[] message = protocol.responseMessage().getMessage();
                        System.out.println("AT响应：" + new String(message));
                    } else {
                        System.out.println("AT响应失败：" + protocol.getExecStatus());
                    }
                    return null;
                });
            }

            //测试读写单个线圈
            ModbusTcpForDtuCommonProtocol.buildWrite05(equipCode, 1, 1, ModbusCoilStatus.ON).request();
            ModbusTcpForDtuCommonProtocol.buildRead01(equipCode, 1, 1, 1).request(protocol -> {
                if (protocol.getExecStatus() == ExecStatus.success) {
                    ModbusTcpForDtuCommonProtocol commonProtocol = (ModbusTcpForDtuCommonProtocol) protocol;
                    ModbusBitStatus modbusBitStatus = commonProtocol.getPayload().readStatus(0);

                    boolean status = (write05 == ModbusCoilStatus.ON && modbusBitStatus == ModbusBitStatus.ON)
                            || (write05 == ModbusCoilStatus.OFF && modbusBitStatus == ModbusBitStatus.OFF);

                    ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
                    logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write05 -> Read01", head.getEquipCode(), head.getMessageId()
                            , status ? "通过" : "失败");
                } else {
                    ModbusTcpHeader head = protocol.requestMessage().getHead();
                    logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write05 -> Read01", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 测试读写多个线圈
            ModbusTcpForDtuCommonProtocol.buildWrite0F(equipCode, 1, 3, write0f).request();
            ModbusTcpForDtuCommonProtocol.buildRead01(equipCode, 1, 3, 8).request(protocol -> {
                ModbusTcpForDtuCommonProtocol commonProtocol = (ModbusTcpForDtuCommonProtocol) protocol;
                ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    Payload payload = commonProtocol.getPayload();
                    logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write0F -> Read01", head.getEquipCode(), head.getMessageId()
                            , write0f[0] == payload.getPayload()[0] ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write0F -> Read01", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 写单个寄存器测试
            ModbusTcpForDtuCommonProtocol.buildWrite06(equipCode, 1, 1, ByteUtil.getBytesOfReverse(write06)).request();
            ModbusTcpForDtuCommonProtocol.buildRead03(equipCode, 1, 1, 1).request(protocol -> {
                ModbusTcpForDtuCommonProtocol commonProtocol = (ModbusTcpForDtuCommonProtocol) protocol;
                ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    short i = commonProtocol.getPayload().readShort(1);
                    logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write06 -> Read03", head.getEquipCode(), head.getMessageId()
                            , i == write06 ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write06 -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            ModbusTcpForDtuCommonProtocol.buildWrite06(equipCode, 1, 2, write06).request();
            ModbusTcpForDtuCommonProtocol.buildRead03(equipCode, 1, 2, 1).request(protocol -> {
                ModbusTcpForDtuCommonProtocol commonProtocol = (ModbusTcpForDtuCommonProtocol) protocol;
                ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    short i = commonProtocol.getPayload().readShort(2);
                    logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write06(arg) -> Read03", head.getEquipCode(), head.getMessageId()
                            , i == write06 ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write06(arg) -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 写多个寄存器测试
            ModbusTcpForDtuCommonProtocol.buildWrite10(equipCode, 1, 3, 2, ByteUtil.getBytes(write10)).request();
            ModbusTcpForDtuCommonProtocol.buildRead03(equipCode, 1, 3, 2).request(protocol -> {
                ModbusTcpForDtuCommonProtocol commonProtocol = (ModbusTcpForDtuCommonProtocol) protocol;
                ModbusTcpHeader head = commonProtocol.requestMessage().getHead();
                if (protocol.getExecStatus() == ExecStatus.success) {
                    int i = commonProtocol.getPayload().readInt(3);
                    logger.info(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write10 -> Read03", head.getEquipCode(), head.getMessageId()
                            , i == write10 ? "通过" : "失败");
                } else {
                    logger.error(TestConst.LOGGER_MODBUS_DESC, component.getName()
                            , "Write10 -> Read03", head.getEquipCode(), head.getMessageId(), "失败");
                }
                return null;
            });

            // 批量读写测试
            ModbusTcpForDtuCommonProtocol.buildWrite10(equipCode, 1, 760, (short)5, 2, 1.8f, 3.5, 300000l, "你好").request();
            ModbusTcpForDtuCommonProtocol.buildRead03(equipCode, 1, 760, 16).request(protocol -> {
                ModbusTcpForDtuCommonProtocol commonProtocol = (ModbusTcpForDtuCommonProtocol) protocol;
                modbusWrite10BatchTest(commonProtocol, protocol.getExecStatus(), 760, component);
                return null;
            });

            long l = System.currentTimeMillis() - start;
            System.out.println("    总耗时：" + l + "(ms)   平均耗时：" + l / 12.0 + "(ms)");
            TimeUnit.SECONDS.sleep(1);
        }

    }

    @Override
    public int getOrder() {
        return 1200;
    }
}
