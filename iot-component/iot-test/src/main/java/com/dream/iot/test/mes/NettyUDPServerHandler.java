package com.dream.iot.test.mes;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dream.iot.test.db.config.entity.SpConfig;
import com.dream.iot.test.db.config.service.ISpConfigService;
import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.service.ISpProductService;
import com.dream.iot.test.rabbitmq.spring.po.Mail;
import com.dream.iot.test.rabbitmq.spring.service.Producer;
import com.dream.iot.test.rabbitmq.spring.service.impl.ProducerImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static com.dream.iot.utils.ByteUtil.bytesToHex;
import static com.dream.iot.utils.ByteUtil.bytesToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lgl
 * @date 2022/08/20
 * @ddescription:
 */

@Component
public class NettyUDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    Logger log = LoggerFactory.getLogger(NettyUDPServerHandler.class);


    @Autowired
    private ISpConfigService iSpConfigService;
    @Autowired
    private ISpProductService iSpProductService;
    @Autowired
    Producer iproducer;
    private static final String[] DICTIONARY = {"what a pity", "shit",
            "gogogogogo"};

    private String nextQuote() {
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        ctx.close();
        cause.printStackTrace();
    }


    public  String GetLineId(String serverIp, long serverPort)
    {
        QueryWrapper<SpConfig> queryWrapper = new QueryWrapper();
        queryWrapper.eq("server_ip", serverIp);
        queryWrapper.eq("server_port",String.valueOf(serverPort));//Long.toString(long)
        List<SpConfig> list = iSpConfigService.list(queryWrapper);
        String lineId = list.get(0).getLineId();

        return lineId;
    }

    /*
    how to read  ByteBuf
    https://blog.csdn.net/qq_22701869/article/details/107091427?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-3-107091427-blog-104410317.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-3-107091427-blog-104410317.pc_relevant_default&utm_relevant_index=5

    ByteBuffer get(byte[] dst)：读取数据到byte[] dst数组中，并且pos会发生变化
    ByteBuffer get(byte[] dst, int offset, int length)：从数组offset下标开始（包含），读取length长度缓存到数组dest
    ByteBuffer的API功能有限，一些高级和实用的特性它不支持，需要使用者自己编程实现。
    为了弥补这些不足，Netty提供了自己的ByteBuffer实现——ByteBuf。
    decodeString(ByteBuffer src, Charset charset)：使用指定的ByteBuffer和charset进行对ByteBuffer进行解码，获取解码后的字符串。
     */
    public  boolean  ParseData(ByteBuf data,String ip,long port) throws Exception {
        int headLen = 14;
        String  productId = "45x47x456.TEST";  //16  byte
        String  seialId = "2022082001";   //16 byte
        String  quality = "OK";  //0-OK ,  1-NG
        short   badPos =-1;
        short  tempShort=0;

        //System.out.println(Arrays.toString(data.array()));

        data.readerIndex(headLen);
        tempShort =data.readShort();
        if(tempShort==0)
        {
            quality="OK";
        }
        else if(tempShort==1)
        {
            quality="NG";
        }
        else
        {
            quality="UNKNOWN";
        }
        badPos = data.readShort();

        data.readerIndex(4*2+headLen);
        float rz1zkL=data.readFloat();
        //float rz1zkH=data.readFloat();
        float rz2zkL=data.readFloat();
        //float rz2zkH=data.readFloat();
        float rz3zkL=data.readFloat();
        //float rz3zkH=data.readFloat();

        float jd1L=data.readFloat();
        //float jd1H=data.readFloat();
        float jd2L=data.readFloat();
        //float jd2H=data.readFloat();
        float jd3L=data.readFloat();
        //float jd3H=data.readFloat();

        System.out.println("jd3L="+jd3L);
        //System.out.println("jd3H="+jd3H);

        float jz1L=data.readFloat();
        float jz1H=data.readFloat();
        float jz2L=data.readFloat();
        float jz2H=data.readFloat();
        float jz3L=data.readFloat();
        float jz3H=data.readFloat();

        System.out.println("jz3h="+jz3H);
        data.readerIndex(30*2+headLen);  //需要加上报头

        byte[] dst =  new byte[16];


        data.getBytes(data.readerIndex(),dst);
        //System.out.println(bytesToString(dst,CharsetUtil.UTF_8));//是acii 不能是UTF_8 ！


        productId = bytesToString(dst);
        data.readerIndex(30*2+headLen+16);

        data.getBytes(data.readerIndex(),dst);
        seialId = bytesToString(dst);


        SpProduct spProduct=new  SpProduct();
        spProduct.setProductId(productId);
        spProduct.setSerialId(seialId);
        spProduct.setQuality(quality);
        spProduct.setBadPos((int) badPos);
        spProduct.setFlowId(GetLineId(ip,port));
        //spProduct.setId("1552594861144850433");
        System.out.println("data引用计数"+data.refCnt());

        iSpProductService.AddProdut(spProduct);
        log.info("spProduct="+spProduct);

/*

        //ProducerImpl producer  = new ProducerImpl();
        //Producer producer  = new Producer();
        Mail mail =  new  Mail();
        mail.setProductId(productType+batchNo);
        mail.setQuality(quality);
        mail.setBadPos((int) badPos);
        iproducer.sendMail("myqueue",mail);
*/
        //data.release();  不能释放，否则引用计数为0 ，就报错了

        return   true;
    }

    /*
    C0 00 02 00 7B 00 00 32 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4F 56 45 52 20 20 34 2E 33 35 20 47 35 2E 32 33 20 47 FF 48 01 13 00 00 00 00 00 00 33 37 58 34 35 58 35 36 37 2E 54 45 53 54 00 00 32 30 32 32 38 31 38 31 33 31 34 36 00 00 00 00 00 00 00 00 00 00 00 00

读命令返回的14字节的报头  ，后面才是100个字节的数据
C0 00 02     固定帧头
00 0B 00     PC网络号，节点号，单元号
00 41 00     设备网络号，节点号，单元号
00 01 01     SID+MRC+SRC
00 00        成功码是0000

     */

    public  boolean   verifyData(ByteBuf data)
    {
        if(data.readableBytes() !=114)
        {
            System.out.println("readableBytes error: " + data.readableBytes()+" byte");
            return false;
        }

        if(data.getByte(0) != (byte)0xC0  || data.getByte(1)!=0x00  )
        {
            System.out.println("head error" );
            return false;
        }
        if( data.getByte(12)!=0x00  && data.getByte(13)!=0x00  )
        {
            System.out.println("fail to read !" );
            return false;
        }


        return true;
    }



    /*
    根据IP 和 port 反推出 工艺线路
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        String req = datagramPacket.content().toString(CharsetUtil.UTF_8);
        ByteBuf data =  datagramPacket.content();
        log.info("ByteBuf: " + data);
        log.info("readableBytes: " + data.readableBytes());//114  多出来的14个字节是 ？
        log.info(req);

        String  ip =datagramPacket.sender().getAddress().toString().substring(1);
        long  port  = datagramPacket.sender().getPort();

        log.info("sender : ip="+ip+ ",  port = "+ port );

        byte[] byteData = new byte[data.readableBytes()];//数据大小
        data.getBytes(data.readerIndex(), byteData);
        log.info(bytesToHex(byteData));
        //System.out.println(bytesToString(byteData,CharsetUtil.UTF_8));
//C00002007B00003200000101000000000000000000000000000000000000000000000000000000000000000000004F5645522020342E33352047352E32332047FF4801130000000000003337583435583536372E544553540000323032323831383133313436000000000000000000000000
//C0 00 02 00 7B 00 00 32 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4F 56 45 52 20 20 34 2E 33 35 20 47 35 2E 32 33 20 47 FF 48 01 13 00 00 00 00 00 00 33 37 58 34 35 58 35 36 37 2E 54 45 53 54 00 00 32 30 32 32 38 31 38 31 33 31 34 36 00 00 00 00 00 00 00 00 00 00 00 00
        if(!verifyData(data))
        {
            log.info("数据验证失败，不再继续解解析" );
            return ;
        }
        ParseData(data,ip,port);

       /* if ("requir".equals(req)) {
            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("result " + nextQuote(), CharsetUtil.UTF_8), datagramPacket.sender()));
        }
*/    }
}