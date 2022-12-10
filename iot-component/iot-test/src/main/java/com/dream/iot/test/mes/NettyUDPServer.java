package com.dream.iot.test.mes;
import com.dream.iot.plc.omron.OmronMessageHeader;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.db.config.entity.SpConfig;
import com.dream.iot.test.db.config.service.ISpConfigService;
import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.service.ISpProductService;
import com.dream.iot.test.db.product.service.impl.SpProductServiceImpl;
import com.dream.iot.test.rabbitmq.spring.po.Mail;
import com.dream.iot.utils.ByteUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
//import io.netty.util.CharsetUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
//import   com.dream.iot.utils.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.*;

import static com.dream.iot.utils.ByteUtil.getBytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lgl
 * @date 2022/08/20
 * @ddescription:
 */


//@Component
public class NettyUDPServer  implements IotTestHandle

{
    public Channel  channel ;
    private  int flag = 0;

    Logger log = LoggerFactory.getLogger(NettyUDPServer.class);

    /* 必须使用 Autowired 装配的方式， 使用new动态生成的 handler 处理消息的时候
     切换上下文，就会释放内存，导致保存数据库失败
     */
    @Autowired
    NettyUDPServerHandler  nettyUDPServerHandler;


    @Autowired
    private ISpConfigService iSpConfigService;
    @Autowired
    private ISpProductService iSpProductService;

    public   void AddProuct(SpProduct spProduct) throws Exception {

        iSpProductService.AddProdut(spProduct);
    }


    //@Scheduled(fixedRate = 30000) //
    public   void sendTest() throws Exception {
        if(channel!=null)
         channel.writeAndFlush("what a fuck");
    }
    @RabbitListener(queues = "myqueue")
    public void displayMail(Mail mail) throws Exception {
        System.out.println("队列监听器1号收到消息" + mail.toString());
        SpProduct record = new SpProduct();
        BeanUtils.copyProperties(mail, record);
        iSpProductService.AddProdut(record);
    }


    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();//线程池
        try {
            SpConfig   config =  iSpConfigService.GetCurrentConfig("磁阻传感器");

            String remoteAddres ="192.168.43.14";
            int remotePort =9600;
            int localPort = 9601;
            if(config!=null) {
                remoteAddres = config.getServerIp();
                remotePort = config.getServerPort();
                localPort = config.getLocalPort();
            }


            log.info("config="+config );

            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioDatagramChannel.class);
            b.option(ChannelOption.SO_BROADCAST, true);

                //new NettyUDPServerHandler()
            b.handler(nettyUDPServerHandler);
            b.remoteAddress(remoteAddres,remotePort);
            ChannelFuture channelF =b.bind(localPort).sync();//.channel();
            channel = channelF.channel();

            myThread t = new myThread(channel);
            t.start();

            channel.closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void start() throws Exception {

        log.info("--- Netty UDP Server start -");

        //sqlTest();
        run();

    }

    @Override
    public int getOrder() {
        return 0;
    }

    class myThread extends Thread{
        private String idNum;
        private Channel  channel;
        private SpConfig   config;

        private  DatagramSocket datagramSocket;
        private String remoteAddres ="192.168.43.14";
        private int remotePort =9600;
        private  int interval =10;

        byte DA1 =14;
        byte SA1=14; //这个omron ,目前可以设置为非1到255之间的非零数字都可以的
        String sendMessage ="www.dreammm.net" ;
        byte[] data=new byte[11];


        public myThread(Channel  channel) throws Exception {
            this.channel= channel;
            config =  iSpConfigService.GetCurrentConfig("磁阻传感器");
            if(config!=null) {
                remoteAddres = config.getServerIp();
                remotePort = config.getServerPort();
                interval = config.getInterv();
                if(StringUtils.hasText(remoteAddres)) {
                    DA1 = (byte) Integer.parseInt(remoteAddres.substring(remoteAddres.lastIndexOf( "." ) + 1));
                }

                String  localhost = InetAddress.getLocalHost().getHostAddress();
                SA1 =  (byte) Integer.parseInt(localhost.substring(localhost.lastIndexOf( "." ) + 1));
                data= buildRequestMsg(SA1, DA1, (short) 2000, (short) 50);   //DM2000 读 50word
            }
        }

        public void run() {

            while (true)
            {
                /*
                channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(sendMessage, CharsetUtil.UTF_8),
                        new InetSocketAddress(remoteAddres, remotePort)));
                */

                channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(data),
                        new InetSocketAddress(remoteAddres, remotePort)));

                try {
                    Thread.currentThread().sleep(1000*interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }


    public byte[] buildRequestMsg(byte SA1, byte DA1, short startPos,short readLength) {

         byte ICF = (byte) 0x80;
         byte RSV = 0x00;
         byte GCT = 0x02;   //(Gateway count)网关数量，一般为0x02。
         byte DNA = 0x00;  //本地网络
         byte DA2 = 0x00;   //PC(CPU)
         byte SNA = 0x00;
         byte SA2 = 0x00;
         byte SID = 0x00;
         byte MRC = 0x01;
         byte SRC = 0x01;


        byte[] message = new byte[] {
                ICF, RSV, GCT,           //固定帧头 80 00 02
                DNA, DA1, DA2,       //设备的网络号，节点号，单元号
                SNA, SA1, SA2,      // PC的网络号，节点号，单元号
                SID, MRC, SRC,              //服务号 sid= 0x00,,//读命令  SID+MRC+SRC
                (byte)0x82,        //DM 区域
                0x07,(byte)0xD0,0x00 ,     // 起始地址
                0x00,0x32               //长度    //total 18 个字节
        };

//读50个字  开始地址 D2000-->07d0   ,32-->50
//800002003200007B000001018207D0000032     18byte
// 800002 003200  007B00 00 0101 82 07D000  0032     ,,,18byte
// 80 00 02 00 0E 00 00 0E 00 00 01 01 82 07 D0 00 00 32    this mine

        byte[] pos = new byte[2];
        pos = getBytes(startPos);
        byte[] len = new byte[2];
        len = getBytes(readLength);

        message[13]=pos[1];
        message[14]=pos[0];
        message[16]=len[1];
        message[17]=len[0];

        return message;
    }


}
