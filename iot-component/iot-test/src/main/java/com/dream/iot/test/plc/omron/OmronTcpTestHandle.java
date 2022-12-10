package com.dream.iot.test.plc.omron;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.plc.omron.OmronTcpProtocol;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.service.ISpProductService;
import com.dream.iot.test.plc.TestPlcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static com.dream.iot.utils.ByteUtil.*;//bytesToHex;

public class OmronTcpTestHandle implements ClientProtocolHandle<OmronTcpProtocol>, IotTestHandle {

    private    OmronTcpProtocol protocol;

    @Autowired
    private ISpProductService iSpProductService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(OmronTcpProtocol protocol) {
        return null;
    }
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 30000) //
    public void RepeatReadData(){
       // System.out.println("每隔5秒执行一次 "+dateFormat.format(new Date()));
        Short wshort =100;// (short) RandomUtil.randomInt(10086);
        //new OmronTcpProtocol().write("D2", wshort);
        //OmronTcpProtocol protocol = new OmronTcpProtocol();
        if(protocol ==null)
            protocol = new OmronTcpProtocol();

        //protocol.write("D2", wshort);
        String  address = "D2000";
        short length =100;
        Short aShort = null; //protocol.readInt16("D2");
        byte[] data = protocol.read(address,length);
        String strHexData= "";
        if(data!=null) strHexData=bytesToHex(data);
        //short aShort = protocol.readInt16("D2");
        //logger.info("Short读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D2", wshort, aShort, wshort == aShort);
        logger.info("读测试 起始地址：{} - 长度:{} - 读值：{} ", address ,length,strHexData);
        String  productCode = "DD20220803";
        String  qulity = "OK";
        Short status =-1;
        Short postion = -1;
        if(data !=null) {
            status = bytesToShort(data, 0);
            postion = bytesToShort(data, 2);
        }
        if(status==0)
            qulity ="OK" ;
        else  if(status==1)  qulity = "NG" ;
        else qulity = "unknown" ;

        //productCode +=RandomUtil.randomNumbers(2 );   //.random.randomString(10);
       if(data !=null)
        productCode = bytesToString(data,60, 92);


        SpProduct spProduct=new  SpProduct();
        spProduct.setProductId(productCode);
        spProduct.setQuality(qulity);
        //spProduct.setId("1552594861144850433");

        iSpProductService.AddProdut(spProduct) ;

    }

    @Override
    public void start() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("--------------------------------------- 开始欧姆龙PLC测试 ------------------------------------------");

        if(false) {
            boolean randomBool = RandomUtil.randomInt(0, 2) == 0 ? false : true;
       /* new OmronTcpProtocol().write("D1.1", randomBool);
        Boolean aBoolean = new OmronTcpProtocol().readBool("D1.1");
        logger.info("布尔读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D1.1", randomBool, aBoolean, randomBool == aBoolean);
*/
            short wshort = (short) RandomUtil.randomInt(10086);
            new OmronTcpProtocol().write("D2", wshort);
            short aShort = new OmronTcpProtocol().readInt16("D2");
            logger.info("Short读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D2", wshort, aShort, wshort == aShort);

            logger.info("over");
            int uShort = RandomUtil.randomInt(Short.MAX_VALUE, Short.MAX_VALUE * 2);
            new OmronTcpProtocol().write("D5", Integer.valueOf(uShort).shortValue());
            Integer uInt16 = new OmronTcpProtocol().readUInt16("D5");
            logger.info("UShort读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D5", uShort, uInt16, uShort == uInt16);

            int winteger = RandomUtil.randomInt();
            new OmronTcpProtocol().write("D10", winteger);
            Integer integer = new OmronTcpProtocol().readInt32("D10");
            logger.info("Int读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D10", winteger, integer, winteger == integer);

            long uInt = RandomUtil.randomLong(Integer.MAX_VALUE, Long.valueOf(Integer.MAX_VALUE) * 2);
            new OmronTcpProtocol().write("D18", Long.valueOf(uInt).intValue());
            Long readUInt32 = new OmronTcpProtocol().readUInt32("D18");
            logger.info("UInt读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D18", uInt, readUInt32, uInt == readUInt32);

            long wlong = RandomUtil.randomLong();
            new OmronTcpProtocol().write("D22", wlong);
            Long aLong = new OmronTcpProtocol().readInt64("D22");
            logger.info("Long读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D22", wlong, aLong, wlong == aLong);

            float wfloat = (float) RandomUtil.randomDouble(164645.35);
            new OmronTcpProtocol().write("D30", wfloat);
            Float aFloat = new OmronTcpProtocol().readFloat("D30");
            logger.info("Float读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D30", wfloat, aFloat, wfloat == aFloat);

            double wdouble = RandomUtil.randomDouble(3, RoundingMode.CEILING);
            new OmronTcpProtocol().write("D38", wdouble);
            Double aDouble = new OmronTcpProtocol().readDouble("D38");
            logger.info("Double读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D38", wdouble, aDouble, wdouble == aDouble);

            String randomString = RandomUtil.randomString(8);
            new OmronTcpProtocol().write("D58", randomString);
            String s = new OmronTcpProtocol().readString("D58", (short) 4); // 这里读取的长度 = 8 / 2
            logger.info("String读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D58", randomString, s, randomString.equals(s));

            System.out.println("------------------------------------- 欧姆龙PLC连续区域块读写测试 ----------------------------------------");

            short[] shorts = TestPlcUtils.randomShorts(2);
            new OmronTcpProtocol().write("D68", shorts);
            short[] readInt16 = new OmronTcpProtocol().readInt16("D68", (short) 2);
            logger.info("Short连续读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D68", shorts, readInt16, TestPlcUtils.arrayEquals(shorts, readInt16));

            int[] randomInts = TestPlcUtils.randomInts(2);
            new OmronTcpProtocol().write("D78", randomInts);
            int[] readInt32 = new OmronTcpProtocol().readInt32("D78", (short) 2);
            logger.info("Int连续读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D78", randomInts, readInt32, TestPlcUtils.arrayEquals(randomInts, readInt32));

            long[] randomLongs = TestPlcUtils.randomLongs(2);
            new OmronTcpProtocol().write("D98", randomLongs);
            long[] readInt64 = new OmronTcpProtocol().readInt64("D98", (short) 2);
            logger.info("Long连续读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D98", randomLongs, readInt64, TestPlcUtils.arrayEquals(randomLongs, readInt64));

            float[] randomFloats = TestPlcUtils.randomFloats(2);
            new OmronTcpProtocol().write("D200", randomFloats);
            float[] readFloat = new OmronTcpProtocol().readFloat("D200", (short) 2);
            logger.info("Float连续读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D200", randomFloats, readFloat, TestPlcUtils.arrayEquals(randomFloats, readFloat));

            double[] randomDoubles = TestPlcUtils.randomDoubles(2);
            new OmronTcpProtocol().write("D250", randomDoubles);
            Double d250 = new OmronTcpProtocol().readDouble("D250");
            double[] readDouble = new OmronTcpProtocol().readDouble("D250", (short) 2);
            logger.info("Double连续读写测试 地址：{} - 写值：{} - 读值：{} - 测试状态：{}", "D250", randomDoubles, readDouble, TestPlcUtils.arrayEquals(randomDoubles, readDouble));

            Long d273 = new OmronTcpProtocol().readUInt32("D273");
            Long d274 = new OmronTcpProtocol().readUInt32("D274");
            int[] d263s = new OmronTcpProtocol().readUInt16("D263", (short) 3);

            System.out.println("------------------------------- 欧姆龙PLC总测试时间：" + (System.currentTimeMillis() - currentTimeMillis) + " (ms) ---------------------------------");
            TimeUnit.SECONDS.sleep(2);

        }
    }

    @Override
    public int getOrder() {
        return 1000 * 20;
    }
}
