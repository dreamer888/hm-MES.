package com.dream.iot.test.modbus.dtu;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.modbus.client.tcp.ModbusTcpClient;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientComponent;
import com.dream.iot.server.dtu.DtuDeviceSnProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ModbusTcpForDtuClientTestComponent extends ModbusTcpClientComponent<ModbusTcpForDtuClientTestMessage> {

    public ModbusTcpForDtuClientTestComponent(ClientConnectProperties config) {
        super(config);
    }

    @Override
    public String getName() {
        return "模拟Dtu设备(Modbus Tcp协议)";
    }

    @Override
    public String getDesc() {
        return "用于测试Modbus Tcp服务(设备使用232或者485连接Dtu)";
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {

        /**
         * 此处的设备编号必须使用TCP_作为前缀
         * @see ModbusDtuTestHandle#handle(DtuDeviceSnProtocol) 此处使用前缀区分是Tcp还是Rtu进行测试
         */
        String deviceSn = "TCP_IOT_MODBUS_01";
        return new ModbusTcpClient(this, config) {

            @Override
            protected void successCallback(ChannelFuture future) {

                // 第一包报文必须是设备编号
                getChannel().writeAndFlush(Unpooled.wrappedBuffer(deviceSn.getBytes(StandardCharsets.UTF_8)));

                // 测试心跳(心跳包 == 设备编号)
                AtomicInteger count = new AtomicInteger();
                ScheduledFuture<?> scheduledFuture = future.channel().eventLoop().scheduleWithFixedDelay(() -> {
                    if(count.get() <= 5) {
                        count.getAndIncrement();
                        getChannel().writeAndFlush(Unpooled.wrappedBuffer(deviceSn.getBytes(StandardCharsets.UTF_8)));
                    } else {
                        future.channel().attr(AttributeKey.<ScheduledFuture>
                                valueOf("IOT:Tcp:ScheduledFuture")).get().cancel(false);
                    }
                }, 3, 30, TimeUnit.SECONDS);

                future.channel().attr(AttributeKey.valueOf("IOT:Tcp:ScheduledFuture")).set(scheduledFuture);
            }
        };
    }

    @Override
    public AbstractProtocol getProtocol(ModbusTcpForDtuClientTestMessage message) {
        return new ModbusTcpForDtuClientTestProtocol(message);
    }

    @Override
    public ModbusTcpForDtuClientTestMessage createMessage(byte[] message) {
        return new ModbusTcpForDtuClientTestMessage(message);
    }
}
