package com.dream.iot.plc.omron;

import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.plc.PlcClientProtocol;
import com.dream.iot.plc.PlcProtocolType;
import com.dream.iot.utils.ByteUtil;

public class OmronMessageHeader extends DefaultMessageHead {

    /*
    lgl
    1、电脑和PLC通讯，通常都是电脑做为客户端，PLC作为服务器端。
    如果是PLC之间进行通讯，任何一台PLC都可以作为服务器或者客户端。

    、2、NX1P2系列PLC内建EtherNet/IP(Port1)的IP位置(IP Address)是什么？
        192.168.250.1
        subnet mask=255.255.255.0

       3,NX1P支持的是HOST LINK(FINS)
       4,  ide :  Sysmac Studio
       5,NX1P2系列PLC支持Modbus-RTU slave协议吗？  不支持。
       6,EtherCAT 用于运动控制
       7,NX1P2内建EIP通讯端口支援FINS/TCP吗？no
       8, 欧姆龙通信协议 FINS 2.0协议地址结构
         https://blog.csdn.net/weixin_34236986/article/details/112710560?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-112710560-blog-42234583.pc_relevant_multi_platform_whitelistv1&spm=1001.2101.3001.4242.1&utm_relevant_index=3

        9 ,INIS协议使用的TCP端口：9600
        FINS协议使用的编码格式为：ASCII

    */

    /**
     * 信息控制字段，默认0x80
     */
    public byte ICF = (byte) 0x80;

    /**
     * 系统使用的内部信息
     * 保留字段
     */
    public byte RSV = 0x00;

    /**
     * 网络层信息，默认0x02，如果有八层消息，就设置为0x07
     */
    public byte GCT = 0x02;   //(Gateway count)网关数量，一般为0x02。

    /**
     * PLC的网络号地址，默认0x00
     * (Destination network address)目标网络地址
     */
    public byte DNA = 0x00;  //本地网络


    //DA1：(Destination node number) PLC目标节点号
    /**
     * PLC的单元号地址，通常都为0
     * //Source unit number)源单元号。
     * 00：PC(CPU)
     *
     * FE：SYSMAC NET连接单元或者SYSMAC LINK单元连接网络
     *
     * 10 to 1F：CPU 总线单元
     */
    public byte DA2 = 0x00;   //PC(CPU)
    /*
    SNA：(Source network address)源网络地址。
    00：本地网络
    01 to 7F：远程网络
     */
    public byte SNA = 0x00;

    /**
     * 上位机的网络号地址
     * SA1：(Source node number)源节点号
     *01 to 7E：SYSMAC NET 网络节点号
     *
     * 01 to 3E：SYSMAC LINK 网络节点号
     *
     * FF：广播节点号
     *
     */

    /**
     * 上位机的单元号地址
     * (Source Unit address)源单元地址
     *00：PC(CPU)
     *
     * FE：SYSMAC NET连接单元或者SYSMAC LINK单元连接网络
     *
     * 10 to 1F：CPU 总线单元
     *
     *
     * a 握手     46494E53 0000000C 00000000(命令) 00000000(错误码) 000000C8
     * b plc回应  46494E53 00000010 00000001(命令) 00000000(错误码)  000000C8(客户机节点地址) 00000001(服务器地址)
     * 这个命令的长度是24字节，分成6组4字节。分别是：头（FINS）+ 长度（00000010） + 命令（00000001） + 错误码 + 客户机节点地址 + 服务器地址。
     *客户端ip地址200即C8，已被服务器01（hex01）成功记录
     * 注意：如果发生错误，服务器回应的命令会包含错误码，连接断开，端口立刻关闭。当连接建立之后，不要再次发送这个命令，否则服务器会返回03错误码，即不支持的命令。
     *
     */
    public byte SA2 = 0x00;

    /**
     * 设备的标识号
     * (Service ID) 序列号 范围00-FF
     */
    public byte SID = 0x00;

    public OmronMessageHeader() {
        super(null, null, PlcProtocolType.Omron);
    }

    protected OmronMessageHeader(byte[] message) {
        super(message);
    }

    protected OmronMessageHeader(String messageId) {
        super(null, messageId, PlcProtocolType.Omron);
    }

    public OmronMessageHeader(String equipCode, String messageId) {
        super(equipCode, messageId, PlcProtocolType.Omron);
    }

        /*
        // 头（FINS）+ 长度（Hex0C） + 命令（00000000） + 错误码00000000 +   客户机节点地址(pc) + 服务器地址（PLC）。  =18个字节
        //FINS header ,
        //FINS command
        //FINS  text

        //这里的message 是type 为02的 命令 的固定格式
        //Fins读取数据是在通用命令基础上，将Parameter替换为Area+Address+Length
        //Fins写数据是在通用命令基础上，将Parameter替换为Area+Address+Length+Value

        不同存储区值不一样，D区：82（DM存储区代码）；W区：B1（W字代码），31（W位代码） ，
        D区或者DM区是一回事，指的是数据存储区，本程序就访问 D区了 。
        例如：读取D500(?) 开始的2个通道：x`x`

        发送：46494E53 0000001A  00000002 00000000   80 00 02 00 01 00 00 C8 00 00   01 01(主次请求码)   82   01F4 00(固定) 0002
        接收：46494E53 0000001A  00000002 00000000   C0 00 02 00 C8 00 00 01 00 00   01 01(主次请求码)   0000(错误码)   00 00 00 01

plc 50--->32,
pc 104  可以随便些 0除外

读d0 ， 1个word
800002003200007B00000101820000000001
C0 00 02 00 7B 00 00 32 00 00 01 01 00 00 01 C8


读50个字  开始地址 D2000-->07d0   ,32-->50
800002003200007B000001018207D0000032

返回100个byte ，
C0 00 02 00 7B 00 00 32 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4F 56 45 52 20 20 34 2E 33 35 20 47 35 2E 32 33 20 47 FF 48 01 13 00 00 00 00 00 00 33 37 58 34 35 58 35 36 37 2E 54 45 53 54 00 00 32 30 32 32 38 31 38 31 33 31 34 36 00 00 00 00 00 00 00 00 00 00 00 00

80 00 00  02 00 32 00 00 68 00 00   01 01   82   0000 00 0002
130 --82
104-->68
50-->32
        组成	  字节数	说明
        Area	1	不同存储区值不一样，D区：82（DM存储区代码）；W区：B1（W字代码），31（W位代码）
        Address	3	起始地址（2bit）+位地址（1bit）
        Length	2	读取或写入长度
        value	2	写入内容（针对写操作）

        //MRC	1	主请求码    //这两个请求吗为何没有提及呢 ？
        //SRC	1	次请求码，主/次请求码组合：0101是读操作；0102是写操作代码；2301是强制操作代码；

         */

    public OmronMessageHeader buildRequestHeader(byte SA1, byte DA1, int bodyLength) {
        byte[] length = ByteUtil.getBytesOfReverse(bodyLength + 18);
        byte[] message = new byte[] {
                0x46, 0x49, 0x4E, 0x53, // FINS
                0x00, 0x00, 0x00, 0x0C, // 18+ bodyLength（命令2byte+错误码2byte+ 客户机节点地址3byte）,,以length的计算为准,
                //18 byte
                0x00, 0x00, 0x00, 0x02,    //命令码: 0x00：connect requst 连接请求数据帧,0x01：connect Response,0x02：data，数据传输；
                0x00, 0x00, 0x00, 0x00,  //错误码，固定为0000
                ICF, RSV, GCT,           //固定帧头 80 00 02
                DNA, DA1, DA2,       //设备的网络号，节点号，单元号
                SNA, SA1, SA2,      // PC的网络号，节点号，单元号
                SID                 //服务号
        };

        this.setMessage(message);
        System.arraycopy(length, 0, message, 4, 4);  //source -->dest
        return this;
    }

    /**
     * @see PlcClientProtocol#sendRequest() 使用channelId作为messageId
     * @param messageId
     */
    @Override
    public void setMessageId(String messageId) {
        super.setMessageId(messageId);
    }
}


/*

欧姆龙PLC的FINS协议解释
UDP访问方式：
读取示例：读取DM区20个字(word), 从DM100H开始
命令:80 00 02  00 41 00   00 0B 00   00(SID)    01 01   82   00 64  00(固定)   00 14
说明：
80 00 02       固定帧头  CF, RSV, GCT,
00 41 00       设备的网络号，节点号，单元号 DNA, DA1, DA2,
00 0B 00       PC的网络号，节点号，单元号 SNA, SA1, SA2,
00 01 01        SID+MRC+SRC      SRC=0x01 读存储区域，0x02写存储区域 ，0x04连续读多个存储区域

82 表示DM区
00 64 首地址
00 固定  !!!!!!
00 14 读取数量

响应: D100=0x1388, D101=0x1770, D102=0x1b58
c0 00 02  00 0b 00   00 41 00   00 01 01    00 00(请求成功返回的成功码是0000)  (后面是数据区)  13 88 17 70 1b 58 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
说明：
C0 00 02    固定帧头
00 0B 00     PC网络号，节点号，单元号
00 41 00     设备网络号，节点号，单元号
00 01 01     SID+MRC+SRC
00 00        成功码是0000
数据区:
13 88 17 70 1b 58 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00

 */