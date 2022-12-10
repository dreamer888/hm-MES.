package com.dream.iot;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.SystemPropertyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 线程管理
 */
public class IotThreadManager implements InitializingBean, DisposableBean, ApplicationContextAware {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ApplicationContext applicationContext;
    private EventExecutor deviceManageEventExecutor;
    private ThreadPoolTaskScheduler schedulerExecutor;
    private static IotThreadManager threadManager = new IotThreadManager();
    private final static String IOT_SERVER_BOOTSTRAP = "com.dream.iot.IotServeBootstrap";
//    private final static String IOT_CLIENT_BOOTSTRAP = "com.dream.iot.client.IotClientBootstrap";

    protected IotThreadManager() { }

    public static IotThreadManager instance() {
        return threadManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Environment environment = this.applicationContext.getEnvironment();
        int bossThreadNum, workerThreadNum = 0;

        // 如果启用服务端
        if(this.applicationContext.containsBeanDefinition(IOT_SERVER_BOOTSTRAP)) {
            bossThreadNum = Integer.valueOf(environment.getProperty("iot.server.boss-thread-num", "1"));

            // IST = Iot Selector Thread
            bossGroup = new NioEventLoopGroup(bossThreadNum, new DefaultThreadFactory("IST"));
        }

        workerThreadNum = Integer.valueOf(environment.getProperty("iot.core.worker-thread-num", "0"));
        if(workerThreadNum == 0) { // 服务端工作线程组
            workerThreadNum = Math.max(1, SystemPropertyUtil.getInt(
                    "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        }
        // IWT = Iot Worker Thread
        workerGroup = new NioEventLoopGroup(workerThreadNum, new DefaultThreadFactory("IWT"));

        // IDM = Iot Device Manager
        this.deviceManageEventExecutor = new DefaultEventExecutor(workerGroup, new DefaultThreadFactory("IDM"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        if(bossGroup != null && !workerGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }

        if(workerGroup != null && !workerGroup.isShutdown()) {
            workerGroup.shutdownGracefully();
        }

        if(!this.deviceManageEventExecutor.isShutdown()) {
            this.deviceManageEventExecutor.shutdownGracefully();
        }
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    /**
     * 设备管理模块使用的调度执行器
     * @return
     */
    public EventExecutor getDeviceManageEventExecutor() {
        return deviceManageEventExecutor;
    }

    public ThreadPoolTaskScheduler getSchedulerExecutor() {
        return schedulerExecutor;
    }

    public void setSchedulerExecutor(ThreadPoolTaskScheduler schedulerExecutor) {
        this.schedulerExecutor = schedulerExecutor;
    }
}
