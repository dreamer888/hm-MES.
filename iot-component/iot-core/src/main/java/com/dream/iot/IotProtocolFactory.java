package com.dream.iot;

/**
 * create time: 2021/2/21
 *  设备协议工厂, 用来生产协议对象
 * @author dream
 * @since 1.0
 */
public interface IotProtocolFactory<T extends SocketMessage> extends StorageManager<String, Protocol>{

    /**
     * 获取协议主要在两种情况下会调用此方法 <br />
     * 1. 客户端响应服务端的请求   因为请求协议会保存在服务端, 所以只需要通过{@link Message.MessageHead#getMessageId()}获取对应的协议 {@link ProtocolTimeoutStorage#remove(String)}
     * 2. 客户端主动请求服务端    这种情况需要开发者创建一个协议对象 通过对应的协议类型 {@link Message.MessageHead#getType()}
     * @param message
     * @return
     */
    AbstractProtocol getProtocol(T message);

    Protocol add(String key, Protocol protocol, long timeout);
}
