package com.dream.iot.test.plc.siemens;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.plc.DataTransfer;
import com.dream.iot.plc.ReadAddress;
import com.dream.iot.plc.siemens.SiemensDataTransfer;
import com.dream.iot.plc.siemens.SiemensS7Protocol;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.plc.TestPlcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SiemensS7TestHandle implements ClientProtocolHandle<SiemensS7Protocol>, IotTestHandle {

    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    private DataTransfer dataTransfer = SiemensDataTransfer.getInstance();
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(SiemensS7Protocol protocol) {
//        System.out.println(ByteUtil.bytesToHex(protocol.responseMessage().getMessage()));
        return null;
    }

    @Override
    public void start() throws Exception {
        System.out.println("--------------------------------------- 开始西门子PLC测试 ------------------------------------------");
        long currentTimeMillis = System.currentTimeMillis();
        new SiemensS7Protocol().write("DB1.0.2", false);
        Boolean aBoolean = new SiemensS7Protocol().readBool("DB1.0.2");
        scheduler.execute(() -> {
            for (int i=0; i<10; i++) {
                new SiemensS7Protocol().readBool("DB1.0.2");
            }
        });
        short wshort = (short) RandomUtil.randomInt(10086);
        new SiemensS7Protocol().write("DB1.2", wshort);
        Short aShort = new SiemensS7Protocol().readInt16("DB1.2");

        int winteger = RandomUtil.randomInt();
        new SiemensS7Protocol().write("DB1.4", winteger);
        Integer integer = new SiemensS7Protocol().readInt32("DB1.4");

        long wlong = RandomUtil.randomLong();
        new SiemensS7Protocol().write("DB1.8", wlong);
        Long aLong = new SiemensS7Protocol().readInt64("DB1.8");

        float wfloat = (float) RandomUtil.randomDouble(1680.35);
        new SiemensS7Protocol().write("DB1.16", wfloat);
        Float aFloat = new SiemensS7Protocol().readFloat("DB1.16");

        double wdouble = RandomUtil.randomDouble(3, RoundingMode.CEILING);
        new SiemensS7Protocol().write("DB1.20", wdouble);
        Double aDouble = new SiemensS7Protocol().readDouble("DB1.20");

        String randomString = RandomUtil.randomString(8);
        new SiemensS7Protocol().write("DB1.58", randomString);
        String s = new SiemensS7Protocol().readString("DB1.58", (short) 8);

        // 批量读测试
        List<byte[]> bytes = new SiemensS7Protocol().batchRead(Arrays
                .asList(
                        ReadAddress.buildBitRead("DB1.0.2"),
                        ReadAddress.buildByteRead("DB1.2", (short) 2),
                        ReadAddress.buildByteRead("DB1.4", (short) 4),
                        ReadAddress.buildByteRead("DB1.8", (short) 8),
                        ReadAddress.buildByteRead("DB1.16", (short) 4),
                        ReadAddress.buildByteRead("DB1.20", (short) 8),
                        ReadAddress.buildByteRead("DB1.58", (short) 8),
                        ReadAddress.buildByteRead("DB1.4", (short) 3),
                        ReadAddress.buildBitRead("DB1.0.2")
                )
        );

        logger.info("布尔读取批量测试 - 地址：{} - 值：{} - 测试状态：{}", "DB1.0.3", aBoolean, aBoolean == (bytes.get(0)[0] == 1));
        short i1 = dataTransfer.toShort(bytes.get(1), 0);
        logger.info("Short读取批量测试 - 地址：{} - 写：{} - 读：{} - 批量读：{} - 测试状态：{}", "DB1.2", wshort, aShort, i1, wshort == i1);
        int i = dataTransfer.toInt(bytes.get(2), 0);
        logger.info("Int读取批量测试 - 地址：{} - 写：{} - 读：{} - 批量读：{} - 测试状态：{}", "DB1.4", winteger, integer, i, winteger == i);
        long l = dataTransfer.toLong(bytes.get(3), 0);
        logger.info("Long读取批量测试 - 地址：{} - 写：{} - 读：{} - 批量读：{} - 测试状态：{}", "DB1.8", wlong, aLong, l, wlong == l);
        float v1 = dataTransfer.toFloat(bytes.get(4), 0);
        logger.info("Float读取批量测试 - 地址：{} - 写：{} - 读：{} - 批量读：{} - 状态：{}", "DB1.16", wfloat, aFloat, v1, wfloat == v1);
        double v = dataTransfer.toDouble(bytes.get(5), 0);
        logger.info("Double读取批量测试 - 地址：{} - 写：{} - 读：{} - 批量读：{} - 状态：{}", "DB1.20", wdouble, aDouble, v, wdouble == v);
        String s1 = new String(bytes.get(6));
        logger.info("String读取批量测试 - 地址：{} - 写：{} - 读：{} - 批量读：{} - 状态：{}", "DB1.58", randomString, s, s1, s1.equals(s));

        System.out.println("---------- 读写连续的内存地址到plc测试 -----------");
        short[] randomShorts = TestPlcUtils.randomShorts(5);
        new SiemensS7Protocol().write("DB1.100", randomShorts);
        short[] shorts = new SiemensS7Protocol().readInt16("DB1.100", (short) 5);
        logger.info("读写连续的short - 地址：{} - 写：{} - 读：{} - 状态：{}", "DB1.100", randomShorts, shorts, TestPlcUtils.arrayEquals(randomShorts, shorts));

        int[] randomInts = TestPlcUtils.randomInts(2);
        new SiemensS7Protocol().write("DB1.300", randomInts);
        int[] int32 = new SiemensS7Protocol().readInt32("DB1.300", (short) 2);
        logger.info("读写连续的int - 地址：{} - 写：{} - 读：{} - 状态：{}", "DB1.300", randomInts, int32, TestPlcUtils.arrayEquals(randomInts, int32));

        long[] longs = TestPlcUtils.randomLongs(2);
        new SiemensS7Protocol().write("DB1.500", longs);
        long[] readInt64 = new SiemensS7Protocol().readInt64("DB1.500", (short) 2);
        logger.info("读写连续的long - 地址：{} - 写：{} - 读：{} - 状态：{}", "DB1.500", longs, readInt64, TestPlcUtils.arrayEquals(longs, readInt64));

        float[] floats = TestPlcUtils.randomFloats(4);
        new SiemensS7Protocol().write("DB1.700", floats);
        float[] readFloat = new SiemensS7Protocol().readFloat("DB1.700", (short) 4);
        logger.info("读写连续的float - 地址：{} - 写：{} - 读：{} - 状态：{}", "DB1.700", floats, readFloat, TestPlcUtils.arrayEquals(floats, readFloat));

        double[] doubles = TestPlcUtils.randomDoubles(2);
        new SiemensS7Protocol().write("DB1.900", doubles);
        double[] readDouble = new SiemensS7Protocol().readDouble("DB1.900", (short) 2);
        logger.info("读写连续的Double - 地址：{} - 写：{} - 读：{} - 状态：{}", "DB1.900", doubles, readDouble, TestPlcUtils.arrayEquals(doubles, readDouble));

        System.out.println("------------------------------ 西门子PLC总测试时间：" + (System.currentTimeMillis() - currentTimeMillis) + " (ms)-------------------------------");
        TimeUnit.SECONDS.sleep(2);
    }

    @Override
    public int getOrder() {
        return 1000 * 10;
    }
}
