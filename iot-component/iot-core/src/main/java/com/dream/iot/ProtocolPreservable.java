package com.dream.iot;

/**
 * 声明是一个可保存的协议, 主要用于协议的同步和异步调用
 * @see ProtocolTimeoutStorage
 */
public interface ProtocolPreservable {

    /**
     * 指定超时时间(ms)
     * @param timeout
     * @return
     */
    Object timeout(long timeout);

    /**
     * 获取超时时间
     * @return
     */
    long getTimeout();

    /**
     * 用来做为将请求报文和响应报文进行关联的key
     * @return
     */
    Object relationKey();

    /**
     * 是否需要将请求的协议报文和响应的协议报文进行关联
     * 由于平台主动发起请求的报文,有时需要得到对方的响应,在做出判断
     * ,所以此协议的报文需要保存起来并且与响应的报文进行匹配(通过消息id)
     * @see #relationKey() 作为两者进行关联的key
     * @return
     */
    default boolean isRelation() {
        return getTimeout() > 0 && relationKey() != null;
    }
}
