package com.dream.iot.websocket;

import com.dream.iot.codec.filter.CombinedFilter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public interface WebSocketFilter<C extends WebSocketComponent> extends CombinedFilter<C> {

    WebSocketFilter DEFAULT = request -> HttpResponseStatus.OK;

    /**
     * websocket请求握手阶段过滤
     * @param channel
     * @param request 升级websocket的请求
     * @return true 继续握手 false 停止握手并响应失败
     */
    default boolean handShaker(Channel channel, HttpRequest request) {
        if (!request.decoderResult().isSuccess()
                || (!"websocket".equals(request.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            return fail(channel, request,  HttpResponseStatus.BAD_REQUEST);
        } else {
            try {
                HttpResponseStatus status = this.authentication(request);
                if(status != HttpResponseStatus.OK) {
                    return fail(channel, request, status);
                }
            } catch (Exception e) {
                return this.fail(channel, request, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return true;
    }

    /**
     * 身份认证在websocket握手时
     * @param request
     * @return {@link  HttpResponseStatus#OK}继续握手  返回其他则停止握手并返回失败
     */
    HttpResponseStatus authentication(HttpRequest request);

    /**
     * 响应客户端失败
     * @param channel
     * @param req
     * @param status 失败状态
     * @return
     */
    default boolean fail(Channel channel, HttpRequest req, HttpResponseStatus status) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), status);
        response.content().writeBytes(status.reasonPhrase().getBytes(StandardCharsets.UTF_8));

        ChannelFuture f = channel.writeAndFlush(response);
        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(req)) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

        return false;
    }
}
