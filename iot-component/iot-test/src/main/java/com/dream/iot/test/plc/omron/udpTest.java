package com.dream.iot.test.plc.omron;



import java.net.DatagramPacket;
        import java.net.DatagramSocket;
        import java.net.InetAddress;

public class udpTest {

    public static void main(String[] args) throws Exception {

        //发送缓冲区
        byte[] buf = "消息内容：this is a Message from Client".getBytes("gbk");
        //数据接收端的地址
        int remotePort = 9600;
        InetAddress remoteAddress = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
        //客户端socket
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, remoteAddress, remotePort);
        socket.send(packet);

        //实例化发送包
        byte[] recieveBuf = new byte[1024];
        DatagramPacket recievePacket = new DatagramPacket(recieveBuf, recieveBuf.length);
        //可以复用socket
        socket.receive(recievePacket);
        String msg = new String(recievePacket.getData());
        System.out.println(msg);


    }

}