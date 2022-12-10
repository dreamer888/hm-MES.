package com.dream.iot.client.protocol;

import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolType;
import com.dream.iot.client.*;
import com.dream.iot.client.component.SingleTcpClientComponent;
import com.dream.iot.client.component.SocketClientComponent;
import com.dream.iot.protocol.socket.AbstractSocketProtocol;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.core.GenericTypeResolver;

/**
 * create time: 2021/8/6
 *
 * @author dream
 * @since 1.0
 */
public abstract class ClientSocketProtocol<C extends ClientMessage> extends AbstractSocketProtocol<C> implements ClientProtocol<C> {

    /**
     * udp协议报文
     */
    private DatagramPacket packet;

    /**
     * 用来获取客户端
     * @see SocketClient
     * @see #getIotClient()
     */
    private ClientConnectProperties clientKey;

    private ClientProtocolHandle defaultProtocolHandle; // 默认协议处理器

    @Override
    public C requestMessage() {
        return super.requestMessage();
    }

    @Override
    public C responseMessage() {
        return super.responseMessage();
    }

    @Override
    public abstract ProtocolType protocolType();

    /**
     * 返回客户端
     * @return
     */



    public SocketClient getIotClient() {

        ClientComponent clientComponent;
        clientComponent= IotClientBootstrap.getClientComponentFactory().getByClass(this.getMessageClass());

        if(clientComponent ==null)
             clientComponent=IotClientBootstrap.getClientComponentFactory().getUdpClientComponent();

        if(clientComponent instanceof SingleTcpClientComponent && getClientKey() != null) {
            throw new ProtocolException("组件["+clientComponent.getName()+"]属于单客户端组件, 不支持使用方法 #request(host, port)");
        }

        Object clientKey = this.getClientKey() != null ? this.getClientKey() : clientComponent.getConfig();
        if(clientKey == null) {
            throw new ClientProtocolException("未找到可用客户端");
        }

        IotClient socketClient = clientComponent.getClient(clientKey);
        if(socketClient instanceof SocketClient) {
            return (SocketClient) socketClient;
        } else {
            /**
             * 防止同一个connectKey建立多个连接
             * @see ClientConnectProperties#connectKey() 声明是同一个连接
             * @see com.dream.iot.client.component.SimpleMultiClientManager#addClient(Object, IotClient) 此处会阻止同一个connectKey添加多次
             */
            synchronized (clientComponent) {
                socketClient = clientComponent.getClient(clientKey);
                if(socketClient != null) {
                    return (SocketClient) socketClient;
                }

                return this.createSocketClient((SocketClientComponent) clientComponent, (ClientConnectProperties)clientKey);
            }
        }
    }

    /**
     * 如果客户端不存在是否创建客户端
     * @param component
     * @param clientKey
     * @return
     */
    protected SocketClient createSocketClient(SocketClientComponent component, ClientConnectProperties clientKey) {
        return component.createNewClientAndConnect(clientKey);
    }

    protected Class<C> getMessageClass() {
        if(requestMessage() != null) {
            return (Class<C>) requestMessage().getClass();
        } else {
            return (Class<C>) GenericTypeResolver.resolveTypeArgument(getClass(), ClientSocketProtocol.class);
        }
    }

    protected String getMessageId() {
        ClientMessage message = requestMessage();
        return message.getMessageId();
    }

    public ClientProtocolHandle getDefaultProtocolHandle() {
        if(defaultProtocolHandle == null) {
            defaultProtocolHandle = (ClientProtocolHandle) IotClientBootstrap
                    .businessFactory.getProtocolHandle(getClass());
        }

        return defaultProtocolHandle;
    }

    public DatagramPacket getPacket() {
        return packet;
    }

    public void setPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public ClientConnectProperties getClientKey() {
        return clientKey;
    }

    public ClientSocketProtocol setClientKey(ClientConnectProperties clientKey) {
        this.clientKey = clientKey;
        return this;
    }
}
