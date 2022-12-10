package com.dream.iot.test.client.fixed;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.CoreConst;
import com.dream.iot.Message;
import com.dream.iot.client.*;
import com.dream.iot.client.codec.FixedLengthFrameClient;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.TestProtocolType;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FixedLengthRequestHandle implements ClientProtocolHandle<FixedLengthRequestProtocol>, IotTestHandle {

    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    @Autowired
    private FixedLengthClientComponent component;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(FixedLengthRequestProtocol protocol) {
        Message.MessageHead head = protocol.requestMessage().getHead();
        String equipCode = head.getEquipCode();
        String desc = "客户端测试";

        if(protocol.getExecStatus() == ExecStatus.success) {
            // 上线数量是值有发送过报文的客户端数量
            logger.info(TestConst.LOGGER_PROTOCOL_DESC+" - 上线数量：{}", component.getName()
                    , desc, equipCode, head.getMessageId(), "通过", protocol.getClientNum());
        } else {
            logger.warn(TestConst.LOGGER_PROTOCOL_DESC, component.getName()
                    , desc, equipCode, head.getMessageId(), "不通过(" + protocol.getExecStatus().desc + ")");
        }
        return null;
    }

    @Override
    public void start() throws Exception {
        System.out.println("------------------------------------------------ 开始协议API测试(客户端固定长度解码器) -----------------------------------------------------");
        ClientConnectProperties config = component.getConfig();
        for(int i=0; i<10; i++) {
            ClientConnectProperties properties = new IotTestProperties.TestMultiConnectConfig();
            BeanUtils.copyProperties(config, properties, "deviceSn");
            component.createNewClientAndConnect(properties);

            try {
                Thread.sleep(RandomUtil.randomInt(300, 800));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 断线重连测试
        List<IotClient> clients = component.clients();
        if (clients.isEmpty()) return;

        int size = component.clients().size();
        ChannelFuture future = (ChannelFuture) component.clients().get(1).disconnect(true);
        future.addListener(future1 -> {
            int newSize = component.clients().size();
            Long deviceSn = (Long)future.channel().attr(CoreConst.EQUIP_CODE).get();
            if(newSize == size - 1) {
                logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "断线移除测试", "disconnect(true)", deviceSn, "通过");
            } else {
                logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "断线移除测试", "disconnect(true)", deviceSn, "失败");
            }
        }).syncUninterruptibly();

        FixedLengthFrameClient iotClient = (FixedLengthFrameClient) component.clients().get(3);
        ChannelFuture reconnect = iotClient.disconnect(false);
        reconnect.addListener(future1 -> {
            ChannelFuture channelFuture = (ChannelFuture) future1;
            Long deviceSn = (Long)iotClient.getChannel().attr(CoreConst.EQUIP_CODE).get();
            if(!channelFuture.channel().isActive()) {
                logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "断线测试", "isActive()", deviceSn, "通过");
                scheduler.execute(() -> { // 避免测试时出现死锁错误提示
                    FixedLengthClientMessage build = FixedLengthClientMessage.build(deviceSn, TestProtocolType.CIReq);
                    new FixedLengthRequestProtocol(build).request(iotClient.getConfig(), protocol -> {
                        if(protocol.getExecStatus() == ExecStatus.success) {
                            logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "断线请求重连测试", "disconnect(false) + request()", deviceSn, "通过");
                        } else {
                            logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "断线请求重连测试", "disconnect(false) + request()", deviceSn, "失败");
                        }

                        return null;
                    });
                });
            } else {
                logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "断线测试", "isActive()", deviceSn, "失败");
            }
        });

        scheduler.execute(() -> {
            final long id = Thread.currentThread().getId();
            // 客户端同步请求测试
            FixedLengthFrameClient syncClient = (FixedLengthFrameClient) component.clients().get(5);
            Long syncDeviceSn = (Long) syncClient.getChannel().attr(CoreConst.EQUIP_CODE).get();
            FixedLengthClientMessage build = FixedLengthClientMessage.build(syncDeviceSn, TestProtocolType.CIReq);
            new FixedLengthRequestProtocol(build).sync(2000).request(syncClient.getConfig(), protocol -> {
                final long id1 = Thread.currentThread().getId();

                if(protocol.getExecStatus() == ExecStatus.success) {
                    if(id == id1) {
                        logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端同步测试", "sync(long) + request()", syncDeviceSn, "通过");
                    } else {
                        logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端同步测试", "sync(long) + request()", syncDeviceSn, "失败");
                    }
                } else {
                    if(id == id1) {
                        logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端同步超时测试", "sync(long) + request()", syncDeviceSn, "通过");
                    } else {
                        logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端同步超时测试", "sync(long) + request()", syncDeviceSn, "失败");
                    }
                }

                return null;
            });

            FixedLengthFrameClient asyncClient = (FixedLengthFrameClient) component.clients().get(7);
            Long asyncDeviceSn = (Long) asyncClient.getChannel().attr(CoreConst.EQUIP_CODE).get();
            FixedLengthClientMessage asyncBuild = FixedLengthClientMessage.build(asyncDeviceSn, TestProtocolType.CIReq);
            new FixedLengthRequestProtocol(asyncBuild).timeout(2000).request(asyncClient.getConfig(), protocol -> {
                final long id1 = Thread.currentThread().getId();
                if(protocol.getExecStatus() == ExecStatus.success) {
                    if(id != id1) {
                        logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端异步测试", "timeout(long) + request()", asyncDeviceSn, "通过");
                    } else {
                        logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端异步测试", "timeout(long) + request()", asyncDeviceSn, "失败");
                    }
                } else {
                    if(id != id1) {
                        logger.info(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端异步超时测试", "timeout(long) + request()", asyncDeviceSn, "通过");
                    } else {
                        logger.error(TestConst.LOGGER_PROTOCOL_FUNC_DESC, "客户端异步超时测试", "timeout(long) + request()", asyncDeviceSn, "失败");
                    }
                }

                return null;
            });
        });

        TimeUnit.SECONDS.sleep(11);
    }

    @Override
    public int getOrder() {
        return 1000 * 40;
    }
}
