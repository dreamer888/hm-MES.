package com.dream.iot.plc.omron;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.codec.DatagramPacketToMessageDecoder;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.client.component.UdpClientComponent;
import com.dream.iot.client.udp.UdpClientConnectProperties;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.event.ClientStatus;
import com.dream.iot.event.ClientStatusEvent;
import com.dream.iot.message.UnParseBodyMessage;
import com.dream.iot.plc.PlcUdpClient;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.nio.ByteOrder;

/**
 * 欧姆龙PLC基于udp 实现的客户端   //lgl
 */
public class OmronUdpClient extends PlcUdpClient {

    public OmronUdpClient(UdpClientComponent clientComponent, UdpClientConnectProperties config) {
        super(clientComponent, config);
    }

    public class  UnParseBodyMessageUdp   extends  UnParseBodyMessage
    {

        public UnParseBodyMessageUdp(byte[] message) {


            super(message);
        }

        @Override
        protected MessageHead doBuild(byte[] message) {
            return null;
        }
    }


    public  class  OmronDatagramPacketToMessageDecoder extends DatagramPacketToMessageDecoder
    {
        @Override
        public  UnParseBodyMessage channelReadMessage(byte[] message)
        {
            UnParseBodyMessageUdp upbm = new  UnParseBodyMessageUdp(message);

            return upbm;
        }

    }

    @Override
    public DatagramPacketToMessageDecoder createProtocolDecoder()
    {

        OmronDatagramPacketToMessageDecoder decoder =  new OmronDatagramPacketToMessageDecoder();
        return decoder;
    }

    @Override
    public UdpClientConnectProperties getConfig() {
        return (UdpClientConnectProperties) super.getConfig();
    }
}
