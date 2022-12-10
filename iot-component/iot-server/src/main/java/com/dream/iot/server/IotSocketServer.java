package com.dream.iot.server;

import com.dream.iot.IotServeBootstrap;
import com.dream.iot.config.ConnectProperties;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 服务端套接字
 */
public interface IotSocketServer {

    /**
     * 监听的端口
     * @see #config()
     * @return
     */
    @Deprecated
    int port();

    /**
     * 服务端配置
     * @see ConnectProperties#getPort()
     * @see ConnectProperties#getHost()
     * @return
     */
    ConnectProperties config();

    /**
     * 开启监听端口并且绑定
     * @param sb
     */
    default void doBind(AbstractBootstrap sb, ApplicationContext context) {
        ChannelFuture bind;
        if(StringUtils.hasText(config().getHost())) {
            bind = sb.bind(config().getHost(), config().getPort());
        } else {
            bind = sb.bind(config().getPort());
        }

        // 绑定此设备要开启的端口
        bind.addListener(future -> {
            SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(config().getPort());

            String host = this.config().getHost() == null ? "0.0.0.0" : this.config().getHost();
            if(future.isSuccess()) {
                LOGGER.info("监听端口成功({}) 主机：{}:{} - 简介：{}", serverComponent.getName(), host, this.port(), serverComponent.getDesc());
            } else {
                LOGGER.error("监听端口失败({}) 主机: {}:{} - 简介：{} - 异常信息: {}", serverComponent.getName(), host, this.port()
                        , serverComponent.getDesc(), future.cause().getMessage(), future.cause());

                Throwable cause = future.cause();
                if(context instanceof ConfigurableApplicationContext) {
                    LOGGER.warn("开启端口失败: {}, 将关闭Spring Application", this.port(), cause);
                    if(((ConfigurableApplicationContext) context).isActive()) {
                        ((ConfigurableApplicationContext) context).close();
                        LOGGER.warn("关闭Spring Application: {} - 状态: 关闭完成", context.getApplicationName());
                    }
                }
            }
        });
    }

    /**
     * 自定义Handler
     * 注：自定义handler的时候需要指定放到那个handler的后面, 整个iot框架对handler的顺序是有要求的
     * @see IotServeBootstrap#afterPropertiesSet()
     * @param p
     */
    default void doInitChannel(ChannelPipeline p) {
        // 加入ssl支持
        if(config().getSsl() != null) {
//            SslContextBuilder.forServer()
            p.addFirst("IOT_SSL", new SslHandler(null));
        }
    }

    /**
     * 返回设备解码器
     * @return
     */
    ChannelInboundHandlerAdapter getMessageDecoder();

    Logger LOGGER = LoggerFactory.getLogger(IotSocketServer.class);
}
