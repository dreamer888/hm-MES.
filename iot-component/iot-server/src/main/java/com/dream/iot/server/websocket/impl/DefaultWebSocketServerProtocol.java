package com.dream.iot.server.websocket.impl;

import com.dream.iot.IotServeBootstrap;
import com.dream.iot.Message;
import com.dream.iot.ProtocolType;
import com.dream.iot.message.DefaultMessageBody;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.SocketServerComponent;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.server.websocket.WebSocketChannelMatcher;
import com.dream.iot.utils.ByteUtil;
import com.dream.iot.websocket.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DefaultWebSocketServerProtocol extends ClientInitiativeProtocol<DefaultWebSocketServerMessage> {

    private DefaultWebSocketServerMessage response;

    public DefaultWebSocketServerProtocol(DefaultWebSocketServerMessage requestMessage) {
        super(requestMessage);
    }

    public String readText() {
        byte[] message = requestMessage().getMessage();
        return new String(message, Charset.defaultCharset());
    }

    public byte[] readBinary() {
        return requestMessage().getMessage();
    }

    /**
     * 响应文本数据
     * @param text
     */
    public void response(String text) {
        Message.MessageHead head = this.requestMessage().getHead();
        DefaultMessageHead messageHead = new DefaultMessageHead(head.getEquipCode(), head.getMessageId(), head.getType());
        this.response = new DefaultWebSocketServerMessage(messageHead,
                new DefaultMessageBody(text.getBytes(StandardCharsets.UTF_8)), WebSocketFrameType.Text);
    }

    /**
     * 响应二进制数据
     * @param binaryData
     */
    public void response(byte[] binaryData) {
        Message.MessageHead head = this.requestMessage().getHead();
        DefaultMessageHead messageHead = new DefaultMessageHead(head.getEquipCode(), head.getMessageId(), head.getType());
        this.response = new DefaultWebSocketServerMessage(messageHead,new DefaultMessageBody(binaryData), WebSocketFrameType.Binary);
    }

    /**
     * 响应报文到请求的设备
     * @param response
     */
    public void response(DefaultWebSocketServerMessage response) {
        this.response = response;
    }

    /**
     * 响应客户端的关闭帧
     * @param rsv
     * @param message
     */
    public void response(int rsv, byte[] message) {
        Message.MessageHead head = requestMessage().getHead();
        WebSocketCloseHead closeHead = new WebSocketCloseHead(head.getEquipCode());
        this.response = new DefaultWebSocketServerMessage(closeHead, new WebSocketCloseBody(rsv, message));
    }

    /**
     * 响应客户端的关闭帧
     * @param status
     * @param reasonText
     */
    public void response(WebSocketCloseStatus status, String reasonText) {
        Message.MessageHead head = requestMessage().getHead();
        WebSocketCloseHead closeHead = new WebSocketCloseHead(head.getEquipCode());
        this.response = new DefaultWebSocketServerMessage(closeHead, new WebSocketCloseBody(status, reasonText)).setFrameType(WebSocketFrameType.Close);
    }

    /**
     * 关闭指定设备连接
     * @param equipCode
     * @param status
     * @param reasonText
     * @return
     */
    public static Optional<ChannelFuture> close(String equipCode, WebSocketCloseStatus status, String reasonText) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            WebSocketCloseHead closeHead = new WebSocketCloseHead(equipCode);
            return serverComponent.writeAndFlush(equipCode,
                    new DefaultWebSocketServerMessage(closeHead, new WebSocketCloseBody(status, reasonText), WebSocketFrameType.Close));
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 关闭指定uri组的所有客户端连接
     * @param uri
     * @param status
     * @param reasonText
     * @return
     */
    public static Optional<ChannelGroupFuture> closeGroup(String uri, WebSocketCloseStatus status, String reasonText) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            WebSocketCloseHead closeHead = new WebSocketCloseHead(null);
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri,
                    new DefaultWebSocketServerMessage(closeHead, new WebSocketCloseBody(status, reasonText), WebSocketFrameType.Close));
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 关闭指定uri组下的匹配的所有客户端连接
     * @param uri
     * @param status
     * @param reasonText
     * @param matcher 过滤组成员
     * @return
     */
    public static Optional<ChannelGroupFuture> closeGroup(String uri, WebSocketCloseStatus status, String reasonText, WebSocketChannelMatcher matcher) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            WebSocketCloseHead closeHead = new WebSocketCloseHead(null);
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri,
                    new DefaultWebSocketServerMessage(closeHead, new WebSocketCloseBody(status, reasonText), WebSocketFrameType.Close), matcher);
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 向指定的设备写出报文
     * @param equipCode 设备编号
     * @param msg 报文
     * @return
     */
    public static Optional<ChannelFuture> write(String equipCode, String msg) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            return serverComponent.writeAndFlush(equipCode,
                    new DefaultWebSocketServerMessage(msg.getBytes(StandardCharsets.UTF_8)).setFrameType(WebSocketFrameType.Text));
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 向指定的设备写出报文
     * @param equipCode 设备编号
     * @param msg 报文
     * @return
     */
    public static Optional<ChannelFuture> write(String equipCode, byte[] msg) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            return serverComponent.writeAndFlush(equipCode
                    , new DefaultWebSocketServerMessage(msg).setFrameType(WebSocketFrameType.Binary));
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 发送报文到所有指定uri的通道
     * @param uri
     * @param text 文本报文 UTF-8
     * @return
     */
    public static Optional<ChannelGroupFuture> writeGroup(String uri, String text) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            DefaultWebSocketServerMessage serverMessage = new DefaultWebSocketServerMessage(text
                    .getBytes(StandardCharsets.UTF_8)).setFrameType(WebSocketFrameType.Text);
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri, serverMessage);
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 发送报文到所有指定uri的通道
     * @param uri
     * @param text 文本报文 UTF-8
     * @param matcher 匹配指定客户端
     * @return
     */
    public static Optional<ChannelGroupFuture> writeGroup(String uri, String text, WebSocketChannelMatcher matcher) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            DefaultWebSocketServerMessage serverMessage = new DefaultWebSocketServerMessage(text
                    .getBytes(StandardCharsets.UTF_8)).setFrameType(WebSocketFrameType.Text);
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri, serverMessage, matcher);
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 发送报文到
     * @param uri
     * @param msg 二进制报文
     * @param matcher 匹配要写入到哪些客户端
     * @return
     */
    public static Optional<ChannelGroupFuture> writeGroup(String uri, byte[] msg, WebSocketChannelMatcher matcher) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            DefaultWebSocketServerMessage message = new DefaultWebSocketServerMessage(msg).setFrameType(WebSocketFrameType.Binary);
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri, message, matcher);
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 发送报文到所有指定uri的通道
     * @param uri
     * @param msg
     * @return
     */
    public static Optional<ChannelGroupFuture> writeGroup(String uri, DefaultWebSocketServerMessage msg) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri, msg);
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    /**
     * 发送报文到
     * @param uri
     * @param msg
     * @param matcher 匹配要写入到哪些客户端
     * @return
     */
    public static Optional<ChannelGroupFuture> writeGroup(String uri, DefaultWebSocketServerMessage msg, WebSocketChannelMatcher matcher) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultWebSocketServerMessage.class);
        if(serverComponent instanceof DefaultWebSocketServerComponent) {
            return ((DefaultWebSocketServerComponent) serverComponent).writeGroup(uri, msg, matcher);
        } else {
            throw new IllegalStateException("未启用websocket组件[DefaultWebSocketServerComponent]");
        }
    }

    @Override
    protected DefaultWebSocketServerMessage doBuildResponseMessage() {
        DefaultWebSocketServerMessage requestMessage = this.requestMessage();

        // 关闭报文
        if(requestMessage.frameType() == WebSocketFrameType.Close && this.response != null) {
            throw new WebSocketException("客户端以主动关闭链接");
        }

        return this.response;
    }

    @Override
    protected void doBuildRequestMessage(DefaultWebSocketServerMessage requestMessage) {
        if(requestMessage.frameType() == WebSocketFrameType.Close) {
            byte[] message = requestMessage.getMessage();
            short statusCode = ByteUtil.bytesToShortOfReverse(message, 0);
            byte[] reason = ByteUtil.subBytes(message, 2);
            requestMessage.setBody(new WebSocketCloseBody(WebSocketCloseStatus
                    .valueOf(statusCode), new String(reason, Charset.defaultCharset())));
        }
    }

    @Override
    public ProtocolType protocolType() {
        return WebSocketProtocolType.Default_Server;
    }
}
