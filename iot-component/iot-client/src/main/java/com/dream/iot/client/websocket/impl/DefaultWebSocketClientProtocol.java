package com.dream.iot.client.websocket.impl;

import com.dream.iot.Message;
import com.dream.iot.ProtocolType;
import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.IotClient;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.codec.WebSocketClient;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.client.websocket.WebSocketClientConnectProperties;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.utils.ByteUtil;
import com.dream.iot.websocket.*;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DefaultWebSocketClientProtocol extends ServerInitiativeProtocol<DefaultWebSocketClientMessage> {

    private String text; // 文本帧数据
    private byte[] binaryData; // 二进制帧数据
    private WebSocketCloseBody closeBody; // 关闭连接帧报文体
    private DefaultWebSocketClientMessage response;

    public DefaultWebSocketClientProtocol(DefaultWebSocketClientMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(DefaultWebSocketClientMessage requestMessage) {
        if(requestMessage.frameType() == WebSocketFrameType.Close) {
            byte[] message = requestMessage.getMessage();
            short statusCode = ByteUtil.bytesToShortOfReverse(message, 0);
            byte[] reason = ByteUtil.subBytes(message, 2);
            requestMessage.setBody(new WebSocketCloseBody(WebSocketCloseStatus
                    .valueOf(statusCode), new String(reason, Charset.defaultCharset())));

            this.closeBody = (WebSocketCloseBody) requestMessage.getBody();
        } else if(requestMessage().frameType() == WebSocketFrameType.Text) {
            this.text = new String(requestMessage().getMessage(), StandardCharsets.UTF_8);
        } else if(requestMessage().frameType() == WebSocketFrameType.Binary) {
            this.binaryData = requestMessage().getMessage();
        } else {

        }
    }

    /**
     * 响应报文到服务端
     * @param response
     */
    public void response(DefaultWebSocketClientMessage response) {
        this.response = response;
    }

    /**
     * 响应报文到服务端
     * @param text
     */
    public void response(String text) {
        String channelId = requestMessage().getChannelId();
        DefaultMessageHead messageHead = new DefaultMessageHead(channelId, null, WebSocketProtocolType.Default_Client);
        this.response = new DefaultWebSocketClientMessage(messageHead, new WebSocketMessageBody(text)).setFrameType(WebSocketFrameType.Text);
    }

    /**
     * 响应报文到服务端
     * @param binaryData
     */
    public void response(byte[] binaryData) {
        String channelId = requestMessage().getChannelId();
        DefaultMessageHead messageHead = new DefaultMessageHead(channelId, null, WebSocketProtocolType.Default_Client);
        this.response = new DefaultWebSocketClientMessage(messageHead, new WebSocketMessageBody(binaryData)).setFrameType(WebSocketFrameType.Binary);
    }

    /**
     * 关闭此客户端
     * @param status
     * @param reasonText
     */
    public void response(WebSocketCloseStatus status, String reasonText) {
        Message.MessageHead head = requestMessage().getHead();
        WebSocketCloseHead closeHead = new WebSocketCloseHead(head.getEquipCode());
        this.response = new DefaultWebSocketClientMessage(closeHead, new WebSocketCloseBody(status, reasonText)).setFrameType(WebSocketFrameType.Close);
    }

    /**
     * 写文本报文到服务端
     * @param properties 客户端配置
     * @param text UTF-8
     * @return
     */
    public static ChannelFuture writer(WebSocketClientConnectProperties properties, String text) {
        ClientComponent component = IotClientBootstrap.getClientComponentFactory().getByClass(DefaultWebSocketClientMessage.class);
        if(component instanceof DefaultWebSocketClientComponent) {
            DefaultWebSocketClientMessage message = new DefaultWebSocketClientMessage(text
                    .getBytes(StandardCharsets.UTF_8)).setFrameType(WebSocketFrameType.Text);
            return ((DefaultWebSocketClientComponent) component).writeAndFlush(properties, message);
        } else {
            throw new WebSocketException("未注入组件["+DefaultWebSocketClientComponent.class+"]");
        }
    }

    /**
     * 写二进制报文到服务端
     * @param properties 客户端配置
     * @param binaryData
     * @return
     */
    public static ChannelFuture writer(WebSocketClientConnectProperties properties, byte[] binaryData) {
        ClientComponent component = IotClientBootstrap.getClientComponentFactory().getByClass(DefaultWebSocketClientMessage.class);
        if(component instanceof DefaultWebSocketClientComponent) {
            DefaultWebSocketClientMessage message = new DefaultWebSocketClientMessage(binaryData).setFrameType(WebSocketFrameType.Binary);
            return ((DefaultWebSocketClientComponent) component).writeAndFlush(properties, message);
        } else {
            throw new WebSocketException("未注入组件["+DefaultWebSocketClientComponent.class+"]");
        }
    }

    /**
     * 关闭指定客户端
     * @param properties 客户端配置
     * @param status 关闭状态
     * @param reasonText 关闭原因
     * @return
     */
    public static ChannelFuture close(WebSocketClientConnectProperties properties, WebSocketCloseStatus status, String reasonText) {
        ClientComponent component = IotClientBootstrap.getClientComponentFactory().getByClass(DefaultWebSocketClientMessage.class);
        if(component instanceof DefaultWebSocketClientComponent) {
            IotClient client = component.getClient(properties);
            if(client instanceof WebSocketClient) {
                WebSocketCloseHead closeHead = new WebSocketCloseHead(null);
                WebSocketCloseBody closeBody = new WebSocketCloseBody(status, reasonText);
                DefaultWebSocketClientMessage message = new DefaultWebSocketClientMessage(closeHead, closeBody).setFrameType(WebSocketFrameType.Close);
                return ((WebSocketClient) client).getChannel().writeAndFlush(message);
            } else {
                throw new WebSocketException("查找不到客户端["+properties.connectKey()+"]");
            }
        } else {
            throw new WebSocketException("未注入组件["+DefaultWebSocketClientComponent.class+"]");
        }
    }

    @Override
    protected DefaultWebSocketClientMessage doBuildResponseMessage() {
        DefaultWebSocketClientMessage requestMessage = this.requestMessage();

        // 如果请求的报文是关闭帧, 客户端将直接关闭请求则不能在响应给请求端
        if(requestMessage.frameType() == WebSocketFrameType.Close) {
            if(this.response != null) {
                throw new WebSocketException("服务端已主动关闭");
            }
        }
        return this.response;
    }

    @Override
    public ProtocolType protocolType() {
        return WebSocketProtocolType.Default_Server;
    }

    public String getText() {
        return text;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public WebSocketCloseBody getCloseBody() {
        return closeBody;
    }
}
