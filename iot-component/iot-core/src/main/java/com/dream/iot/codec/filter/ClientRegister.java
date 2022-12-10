package com.dream.iot.codec.filter;

import com.dream.iot.FrameworkComponent;
import com.dream.iot.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * 客户端编号注册和连接过滤
 */
public interface ClientRegister<C extends FrameworkComponent> extends Filter<C> {

    /**
     * 此连接是否可激活
     * @see ChannelInboundHandler#channelActive(ChannelHandlerContext)
     * @param channel
     * @param component
     * @return true：允许客户端连接  false：直接关闭连接
     */
    default boolean isActivation(Channel channel, C component) {
        return true;
    }

    /**
     * 客户端编号注册, 一个链接只注册一次
     * 主要用于将连接和客户端编号进行绑定
     * @see Channel#attr(AttributeKey) {@link com.dream.iot.CoreConst#EQUIP_CODE} 如果没有此属性将注册
     * @param head {@link com.dream.iot.SocketMessage#doBuild(byte[])}
     * @param params 注册参数
     * @return @param head
     */
    default Message.MessageHead register(Message.MessageHead head, RegisterParams params) {
        return head;
    }

}
