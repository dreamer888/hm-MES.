package com.dream.iot.codec.filter;

import com.dream.iot.FrameworkComponent;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCounted;

/**
 * 解码过滤, 在解码之前调用
 */
public interface DecoderFilter<C extends FrameworkComponent> extends Filter<C> {

    /**
     * 是否需要解码
     * @param channel
     * @param msg
     * @return {@code Boolean} 返回true继续解码  false放弃解码  如果要丢弃此报文需要自行处理{@link ReferenceCounted#release()}
     */
    default boolean isDecoder(Channel channel, ReferenceCounted msg) {
        return msg == null ? false : true;
    }
}
