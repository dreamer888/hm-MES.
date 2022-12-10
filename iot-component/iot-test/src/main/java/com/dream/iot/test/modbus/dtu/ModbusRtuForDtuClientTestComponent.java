package com.dream.iot.test.modbus.dtu;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.codec.SimpleChannelInboundClient;
import com.dream.iot.client.component.SimpleChannelInboundClientComponent;
import com.dream.iot.server.dtu.DtuDeviceSnProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ModbusRtuForDtuClientTestComponent extends SimpleChannelInboundClientComponent<ModbusRtuForDtuClientTestMessage> {

    public ModbusRtuForDtuClientTestComponent(ClientConnectProperties config) {
        super(config);
    }

    @Override
    public String getName() {
        return "模拟Dtu设备(Modbus Rtu协议)";
    }

    @Override
    public String getDesc() {
        return "用于测试Modbus Rtu服务(设备使用232或者485连接Dtu)";
    }

    @Override
    public SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf decode) {
        int i = decode.readableBytes();
        if(i > 0) {
            try {
                byte[] message = new byte[i];
                decode.readBytes(message);
                return new ModbusRtuForDtuClientTestMessage(message);
            } finally {
                decode.release();
            }
        }

        return null;
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        /**
         * 此处的设备编号必须使用RTU_作为前缀
         * @see ModbusDtuTestHandle#handle(DtuDeviceSnProtocol) 此处使用前缀区分是Tcp还是Rtu进行测试
         */
        String rtuDeviceSn = "RTU_IOT_MODBUS_01";
        return new SimpleChannelInboundClient(this, config) {

            @Override
            protected void successCallback(ChannelFuture future) {

                // 第一包报文必须是设备编号
                getChannel().writeAndFlush(Unpooled.wrappedBuffer(rtuDeviceSn.getBytes(StandardCharsets.UTF_8)));

                // 测试心跳(心跳包 == 设备编号)
                AtomicInteger count = new AtomicInteger();
                ScheduledFuture<?> scheduledFuture = future.channel().eventLoop().scheduleWithFixedDelay(() -> {
                    if(count.get() <= 5) {
                        count.getAndIncrement();
                        getChannel().writeAndFlush(Unpooled.wrappedBuffer(rtuDeviceSn.getBytes(StandardCharsets.UTF_8)));
                    } else {
                        future.channel().attr(AttributeKey.<ScheduledFuture>
                                valueOf("IOT:Rtu:ScheduledFuture")).get().cancel(false);
                    }
                }, 3, 30, TimeUnit.SECONDS);

                future.channel().attr(AttributeKey.valueOf("IOT:Rtu:ScheduledFuture")).set(scheduledFuture);
            }
        };
    }

    @Override
    public AbstractProtocol getProtocol(ModbusRtuForDtuClientTestMessage message) {
        return new ModbusRtuForDtuClientTestProtocol(message);
    }
}
