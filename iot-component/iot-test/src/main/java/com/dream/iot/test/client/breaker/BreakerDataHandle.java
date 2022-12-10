package com.dream.iot.test.client.breaker;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.client.SocketClient;
import com.dream.iot.taos.TaosSqlManager;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.taos.TaosBreakerUsingStable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BreakerDataHandle implements IotTestHandle {

    @Autowired
    private IotTestProperties properties;

    private IotTestProperties.BreakerConnectConfig config;

    @Autowired
    private BreakerClientComponent component;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Autowired
    private TaosSqlManager taosSqlManager;

    public BreakerDataHandle(IotTestProperties.BreakerConnectConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        System.out.println("-------------------------------------------- 断路器模拟测试(高并发、大数据、存储[redis、taos]) ---------------------------------------------");
        // 创建客户端
        int num = config.getNum();
        if(num <= 0) return;

        List<Object> usingStables = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            IotTestProperties.BreakerConnectConfig connectConfig = new IotTestProperties.BreakerConnectConfig();
            connectConfig.setPort(config.getPort());
            connectConfig.setHost(config.getHost());

            usingStables.add(new TaosBreakerUsingStable(connectConfig.getDeviceSn()));
            component.createNewClientAndConnect(connectConfig);
        }

        // 如果启用taos测试, 先创建数据表
        if(properties.isTaosStart()) {
            IotTestProperties.BreakerConnectConfig config = (IotTestProperties.BreakerConnectConfig) component.getConfig();
            usingStables.add(new TaosBreakerUsingStable(config.getDeviceSn()));
            taosSqlManager.batchUpdate(TaosBreakerUsingStable.class, usingStables, 300);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int poolSize = scheduler.getScheduledThreadPoolExecutor().getCorePoolSize();
        if(num > poolSize) {
            int numThreadOfPeer = component.clients().size() / poolSize;
            for(int i=0; i < poolSize; i++) {
                int startIndex = i * numThreadOfPeer;
                int endIndex = startIndex + numThreadOfPeer;
                if(i == poolSize - 1) {
                    endIndex = endIndex + component.clients().size() % poolSize;
                }

                List<SocketClient> iotClients = component.clients().subList(startIndex, endIndex)
                        .stream().map(item -> (SocketClient) item).collect(Collectors.toList());


                // 每随机定时任务周期
                int delay = RandomUtil.randomInt(28, 88);
                scheduler.scheduleWithFixedDelay(new PushTask(iotClients, config.getCountOfPeer())
                        , Instant.now().plusSeconds(3), Duration.ofSeconds(delay));
            }
        } else {
            component.clients().stream()
                    .map(item -> (SocketClient) item).forEach(item -> {

                // 每30秒发送一次数据
                scheduler.scheduleWithFixedDelay(new PushTask(Arrays.asList(item), config.getCountOfPeer())
                        , Instant.now().plusSeconds(3), Duration.ofSeconds(30));
            });

        }
    }

    @Override
    public int getOrder() {
        return 1000 * 90;
    }

    public class PushTask implements Runnable {

        private int countOfPeer;
        private List<SocketClient> socketClients;

        public PushTask(List<SocketClient> socketClients, int countOfPeer) {
            this.countOfPeer = countOfPeer;
            this.socketClients = socketClients;
        }

        @Override
        public void run() {
            try {
                int random = RandomUtil.randomInt(0, 9);
                if(random % 2 == 0) {
                    this.socketClients.forEach(item -> {
                        for (int i = 0; i < this.countOfPeer; i++) {
                            IotTestProperties.BreakerConnectConfig connectConfig = (IotTestProperties.BreakerConnectConfig) item.getConfig();
                            new DataPushProtocol(connectConfig.getDeviceSn()).request(item.getConfig());
                        }
                    });
                } else {
                    for (int i = 0; i < this.countOfPeer; i++) {
                        this.socketClients.forEach(item -> {
                            IotTestProperties.BreakerConnectConfig connectConfig = (IotTestProperties.BreakerConnectConfig) item.getConfig();
                            new DataPushProtocol(connectConfig.getDeviceSn()).request(item.getConfig());
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
