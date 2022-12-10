package com.dream.iot.test.plc.omron;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.plc.omron.OmronConnectProperties;
import com.dream.iot.plc.omron.OmronTcpProtocol;
import com.dream.iot.plc.omron.OmronUdpConnectProperties;
import com.dream.iot.plc.omron.OmronUdpProtocol;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.db.config.entity.SpConfig;
import com.dream.iot.test.db.config.service.impl.SpConfigServiceImpl;
import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.service.ISpProductService;
import com.dream.iot.test.plc.TestPlcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dream.iot.utils.ByteUtil.*;


//@Component
public class OmronUdpTestHandle implements ClientProtocolHandle<OmronUdpProtocol>, IotTestHandle {

    private    OmronUdpProtocol protocol;

    private   SpConfig spConfig;

    public OmronUdpConnectProperties properties;

    @Autowired
    SpConfigServiceImpl spConfigServiceImpl;



    @Autowired
    private ISpProductService iSpProductService;

    private Logger logger = LoggerFactory.getLogger(getClass());


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    public  OmronUdpConnectProperties GetProperties()
    {
        if (properties !=null)
            return properties;

        String remoteHost="127.0.0.1";
        Integer remotePort=9600;
        String localHost="127.0.0.1";
        Integer localPort=9609;

        if(false) {
            QueryWrapper<SpConfig> queryWrapper = new QueryWrapper();
            queryWrapper.like("lineId", "1336868662673440");   //磁阻变压器

            if (spConfigServiceImpl == null)
                spConfigServiceImpl = new SpConfigServiceImpl();
            //List<SpConfig> list = spConfigServiceImpl.list(queryWrapper);
            List<SpConfig> list = spConfigServiceImpl.list();
            //SpConfig spConfig = iSpConfigService.getById(list.get(0).getId());
            SpConfig spConfig = list.get(0);
            spConfig = list.get(0);
        }

         properties =  new OmronUdpConnectProperties(remoteHost, remotePort, localHost, localPort);

        return  properties;
    }



    @Scheduled(fixedRate = 60000) //
    public void RepeatReadData(){

        if(properties==null)
            properties = GetProperties();
        //spConfig = IotTestProperties.TestOmronUdpConnectProperties.GetCurrentConfig();
         //这个是 config 是 读取成功了的

       // System.out.println("每隔5秒执行一次 "+dateFormat.format(new Date()));
        Short wshort =100;// (short) RandomUtil.randomInt(10086);
        //new OmronTcpProtocol().write("D2", wshort);
        //OmronTcpProtocol protocol = new OmronTcpProtocol();
        if(protocol ==null)
            protocol = new OmronUdpProtocol(properties);

        //protocol.write("D2", wshort);
        String  address = "D2000";
        short length =50;  //50 word = 100  byte
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
        System.out.println("--------------------------------------- 开始欧姆龙PLC  udp 测试 ------------------------------------------");


    }

    @Override
    public int getOrder() {
        return 1000 * 20;
    }

    @Override
    public Class<OmronUdpProtocol> protocolClass() {
        return ClientProtocolHandle.super.protocolClass();
    }

    @Override
    public Object handle(OmronUdpProtocol protocol) {
        return null;
    }
}
