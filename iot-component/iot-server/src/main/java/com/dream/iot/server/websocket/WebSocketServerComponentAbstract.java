package com.dream.iot.server.websocket;

import com.dream.iot.IotThreadManager;
import com.dream.iot.codec.filter.CombinedFilter;
import com.dream.iot.server.codec.WebSocketServerDecoder;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.TcpDecoderServerComponent;
import com.dream.iot.websocket.WebSocketEncoder;
import com.dream.iot.websocket.WebSocketFilter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.dream.iot.CoreConst.SERVER_ENCODER_HANDLER;

public abstract class WebSocketServerComponentAbstract<M extends WebSocketServerMessageAbstract> extends TcpDecoderServerComponent<M> implements WebSocketServerComponent<M> {

    private boolean allowExtensions = false;
    private WebSocketEncoder webSocketEncoder = new WebSocketEncoder(this);
    private Map<String, ChannelGroup> uriGroup = new ConcurrentHashMap<>(36);
    private WebSocketServerDecoder decoderAdapter = new WebSocketServerDecoder(this);

    public WebSocketServerComponentAbstract(ConnectProperties connectProperties) {
        super(connectProperties);
    }

    /**
     * uri对应的一组连接, uri不包含查询参数：/test
     * @return {@link ChannelGroup} or null
     */
    public Optional<ChannelGroup> group(String uri) {
        return Optional.ofNullable(uriGroup.get(uri));
    }

    public Optional<ChannelGroupFuture> writeGroup(String uri, Object msg) {
        ChannelGroup channels = uriGroup.get(uri);
        if(channels != null) {
            return Optional.of(channels.writeAndFlush(msg));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ChannelGroupFuture> writeGroup(String uri, Object msg, ChannelMatcher matcher) {
        ChannelGroup channels = uriGroup.get(uri);
        if(channels != null) {
            return Optional.of(channels.writeAndFlush(msg, matcher));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void doInitChannel(ChannelPipeline p) {
        super.doInitChannel(p);
        // 大文件拆分处理, 请在子Component自行覆写加入
//        p.addFirst("IOT:CHUNKED", new ChunkedWriteHandler());
        // 注：netty自己会处理
//        p.addFirst("IOT:WEBSOCKET:AGGREGATOR",new HttpObjectAggregator(65536));
        // 替换默认的编码器到WebSocket编码
        p.replace(SERVER_ENCODER_HANDLER, SERVER_ENCODER_HANDLER, webSocketEncoder);
        p.addFirst("IOT:HTTP:CODEC", new HttpServerCodec());
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return decoderAdapter;
    }

    @Override
    public WebSocketServerHandshaker createServerHandShaker(ChannelHandlerContext ctx, HttpRequest req) {
        String url = req.uri();
        int i = req.uri().indexOf("?");
        if(i > 0) {
            url = req.uri().substring(0, i);
        }

        ChannelGroup channels = uriGroup.get(url);
        if (channels == null) {
            synchronized (this) {
                channels = uriGroup.get(url);
                if (channels == null) {
                    uriGroup.put(url, channels = new DefaultChannelGroup(url
                            , IotThreadManager.instance().getDeviceManageEventExecutor()));
                }
            }
        }

        channels.add(ctx.channel());
        ConnectProperties config = config();
        String webSocketUrl;
        if(config.getSsl() != null) {
            webSocketUrl = "wss://" + config.getHost()+":"+config.getPort()+url;
        } else {
            webSocketUrl = "ws://" + config.getHost()+":"+config.getPort()+url;
        }

        return this.doCreateServerHandShaker(webSocketUrl, req);
    }

    protected WebSocketServerHandshaker doCreateServerHandShaker(String webSocketUrl, HttpRequest req) {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                webSocketUrl, null, allowExtensions);
        return wsFactory.newHandshaker(req);
    }

    @Override
    public Optional<ChannelFuture> close(String equipCode) {
        Channel channel = getDeviceManager().find(equipCode);
        if(channel != null) {
            if(channel.isActive()) {
                return Optional.of(channel.writeAndFlush(new CloseWebSocketFrame()).addListener(ChannelFutureListener.CLOSE));
            } else {
                return Optional.of(channel.newSucceededFuture());
            }
        }

        return Optional.empty();
    }

    public Optional<ChannelFuture> close(String equipCode, int statusCode, String reasonText) {
        Channel channel = getDeviceManager().find(equipCode);
        if(channel != null) {
            if(channel.isActive()) {
                return Optional.of(channel.writeAndFlush(new CloseWebSocketFrame(statusCode
                        , reasonText)).addListener(ChannelFutureListener.CLOSE));
            } else {
                return Optional.of(channel.newSucceededFuture());
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<ChannelFuture> close(String equipCode,boolean finalFragment, int rsv, ByteBuf binaryData) {
        Channel channel = getDeviceManager().find(equipCode);
        if(channel != null) {
            if(channel.isActive()) {
                return Optional.of(channel.writeAndFlush(new CloseWebSocketFrame(finalFragment
                        , rsv, binaryData)).addListener(ChannelFutureListener.CLOSE));
            } else {
                return Optional.of(channel.newSucceededFuture());
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CombinedFilter> getFilter() {
        Optional<CombinedFilter> filter = super.getFilter();
        if(filter.isPresent()) {
            if(filter.get() instanceof WebSocketFilter) {
                return filter;
            }else {
                throw new IllegalArgumentException("WebSocket只支持类型["+WebSocketFilter.class.getSimpleName()+"]");
            }
        }

        return filter;
    }

    public boolean isAllowExtensions() {
        return allowExtensions;
    }

    public void setAllowExtensions(boolean allowExtensions) {
        this.allowExtensions = allowExtensions;
    }
}
