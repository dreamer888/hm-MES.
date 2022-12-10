package com.dream.iot.client;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.codec.ByteToMessageDecoderClient;
import com.dream.iot.client.component.DefaultClientComponent;
import com.dream.iot.IotProtocolFactory;
import com.dream.iot.codec.adapter.ByteToMessageDecoderAdapter;
import com.dream.iot.codec.adapter.DelimiterBasedFrameMessageDecoderAdapter;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.codec.adapter.LineBasedFrameMessageDecoderAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandler;
import org.springframework.core.GenericTypeResolver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.nio.ByteOrder;

/**
 * 客户端组件构建器
 * @see ClientComponent
 */
public class ClientBuilder {

    /**
     * 使用换行符解码器
     * @see io.netty.handler.codec.LineBasedFrameDecoder
     * @see LineBasedFrameMessageDecoderAdapter
     * @return
     */
    public static LineBasedFrameDecoderBuilder useLineBasedFrameDecoder() {
        return new LineBasedFrameDecoderBuilder();
    }

    /**
     * 使用长度字段解码
     * @see LengthFieldBasedFrameMessageDecoderAdapter
     * @see io.netty.handler.codec.LengthFieldBasedFrameDecoder
     * @return
     */
    public static LengthFieldBasedFrameDecoderBuilder useLengthFieldBasedFrameDecoder() {
        return new LengthFieldBasedFrameDecoderBuilder();
    }

    /**
     * 自定义分隔符解码
     * @see DelimiterBasedFrameMessageDecoderAdapter
     * @see io.netty.handler.codec.DelimiterBasedFrameDecoder
     * @return
     */
    public static DelimiterBasedFrameDecoderBuilder useDelimiterBasedFrameDecoder() {
        return new DelimiterBasedFrameDecoderBuilder();
    }

    /**
     * 使用自定义格式解码
     * @see ByteToMessageDecoderAdapter
     * @see io.netty.handler.codec.ByteToMessageDecoder
     * @return
     */
    public static ByteToMessageDecoderBuilder useByteToMessageDecoder() {
        return new ByteToMessageDecoderBuilder();
    }

    protected ClientComponent build(CommonConfig build) {
        ClientBuilder builder = build.builder;
        // 使用客户端工厂构建客户端
        if(build.clientFactory != null) {
            return new DefaultClientComponent(build.name, build.name
                    , build.config, build.clientFactory, build.factory, build.messageClass);
        }

        // 使用单一客户端构建组件
        SocketClient socketClient = null;
        if(builder instanceof LineBasedFrameDecoderBuilder) {
            LineBasedFrameDecoderBuilder config = (LineBasedFrameDecoderBuilder) builder;

            socketClient = new TcpSocketClient(null, build.config) {
                @Override
                protected ChannelInboundHandler createProtocolDecoder() {
                    return new LineBasedFrameMessageDecoderAdapter(config.maxLength, config.stripDelimiter, config.failFast);
                }
            };
        } else if(builder instanceof LengthFieldBasedFrameDecoderBuilder) {
            LengthFieldBasedFrameDecoderBuilder config = (LengthFieldBasedFrameDecoderBuilder) builder;

            socketClient = new TcpSocketClient(null, build.config) {
                @Override
                protected ChannelInboundHandler createProtocolDecoder() {
                    return new LengthFieldBasedFrameMessageDecoderAdapter(config.byteOrder, config.maxFrameLength
                            , config.lengthFieldOffset, config.lengthFieldLength, config.lengthAdjustment, config.initialBytesToStrip, config.failFast) {
                        @Override
                        public Class<? extends SocketMessage> getMessageClass() {
                            return build.messageClass;
                        }
                    };
                }
            };
        } else if(builder instanceof DelimiterBasedFrameDecoderBuilder) {
            DelimiterBasedFrameDecoderBuilder config = (DelimiterBasedFrameDecoderBuilder) builder;

            socketClient = new TcpSocketClient(null, build.config) {
                @Override
                protected ChannelInboundHandler createProtocolDecoder() {
                    return new DelimiterBasedFrameMessageDecoderAdapter(config.maxFrameLength, config.stripDelimiter, config.failFast, config.delimiters) {
                        @Override
                        public Class<? extends SocketMessage> getMessageClass() {
                            return build.messageClass;
                        }
                    };
                }
            };
        } else if(builder instanceof ByteToMessageDecoderBuilder) {
            socketClient = ((ByteToMessageDecoderBuilder) builder).decoderClient;
        }

        return new DefaultClientComponent(socketClient, build.factory, build.messageClass).setName(build.name);
    }

    public static class ByteToMessageDecoderBuilder extends ClientBuilder {

        private ByteToMessageDecoderClient decoderClient;

        /**
         * 自定义解码器
         * @param client
         * @return
         */
        public CommonConfig customizeDecode(ByteToMessageDecoderClient client) {
            this.decoderClient = client;
            return new CommonConfig(this);
        }
    }

    public static class DelimiterBasedFrameDecoderBuilder extends ClientBuilder {
        private ByteBuf[] delimiters;
        private int maxFrameLength;
        private boolean failFast = true;
        private boolean stripDelimiter = true;

        public DelimiterBasedFrameDecoderBuilder delimiters(ByteBuf[] delimiters) {
            this.delimiters = delimiters;
            return this;
        }

        public DelimiterBasedFrameDecoderBuilder maxFrameLength(int maxFrameLength) {
            this.maxFrameLength = maxFrameLength;
            return this;
        }

        public DelimiterBasedFrameDecoderBuilder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        public DelimiterBasedFrameDecoderBuilder stripDelimiter(boolean stripDelimiter) {
            this.stripDelimiter = stripDelimiter;
            return this;
        }

        public CommonConfig then() {
            return new CommonConfig(this);
        }
    }

    public static class LengthFieldBasedFrameDecoderBuilder extends ClientBuilder {
        private boolean failFast;
        private int maxFrameLength;
        private int lengthFieldOffset;
        private int lengthFieldLength;
        private int lengthAdjustment = 0;
        private int initialBytesToStrip = 0;
        private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        public LengthFieldBasedFrameDecoderBuilder byteOrder(ByteOrder byteOrder) {
            this.byteOrder = byteOrder;
            return this;
        }

        public LengthFieldBasedFrameDecoderBuilder maxFrameLength(int maxFrameLength) {
            this.maxFrameLength = maxFrameLength;
            return this;
        }

        public LengthFieldBasedFrameDecoderBuilder lengthFieldOffset(int lengthFieldOffset) {
            this.lengthFieldOffset = lengthFieldOffset;
            return this;
        }

        public LengthFieldBasedFrameDecoderBuilder lengthFieldLength(int lengthFieldLength) {
            this.lengthFieldLength = lengthFieldLength;
            return this;
        }

        public LengthFieldBasedFrameDecoderBuilder lengthAdjustment(int lengthAdjustment) {
            this.lengthAdjustment = lengthAdjustment;
            return this;
        }

        public LengthFieldBasedFrameDecoderBuilder initialBytesToStrip(int initialBytesToStrip) {
            this.initialBytesToStrip = initialBytesToStrip;
            return this;
        }

        public LengthFieldBasedFrameDecoderBuilder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        public CommonConfig then() {
            return new CommonConfig(this);
        }
    }

    public static class LineBasedFrameDecoderBuilder extends ClientBuilder {

        private int maxLength;
        /** Whether or not to throw an exception as soon as we exceed maxLength. */
        private boolean failFast = false;
        private boolean stripDelimiter = true;

        public LineBasedFrameDecoderBuilder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public LineBasedFrameDecoderBuilder failFast(boolean failFast) {
            this.failFast = failFast;
            return this;
        }

        public LineBasedFrameDecoderBuilder stripDelimiter(boolean stripDelimiter) {
            this.stripDelimiter = stripDelimiter;
            return this;
        }

        public CommonConfig then() {
            return new CommonConfig(this);
        }
    }

    public static class CommonConfig {

        private String name;
        private IotProtocolFactory factory;
        private ClientFactory clientFactory;
        private ClientConnectProperties config;
        private Class<? extends ClientMessage> messageClass;

        private ClientBuilder builder;

        public CommonConfig(ClientBuilder builder) {
            this.builder = builder;
        }

        /**
         * 组件说明  比如：断路器设备服务
         * @param name
         * @return
         */
        public CommonConfig profile(@Nullable String name) {
            this.name = name;
            return this;
        }

        public CommonConfig connect(ClientConnectProperties config) {
            this.config = config;
            return this;
        }

        public CommonConfig connect(String host, int port) {
            this.config = new ClientConnectProperties(host, port);
            return this;
        }

        public CommonConfig useMessage(@NonNull Class<? extends ClientMessage> messageClass) {
            this.messageClass = messageClass;
            return this;
        }

        public CommonConfig useClientFactory(ClientFactory factory) {
            if(factory == null) {
                throw new IllegalArgumentException("参数[factory]必填");
            }

            this.clientFactory = factory;
            return this;
        }

        public CommonConfig useFactory(IotProtocolFactory factory) {
            this.factory = factory;
            if(messageClass == null) {
                Class<?> aClass = GenericTypeResolver.resolveTypeArgument(factory.getClass(), IotProtocolFactory.class);
                this.messageClass = (Class<? extends ClientMessage>) aClass;
            }
            return this;
        }

        public ClientComponent build() {
            return builder.build(this);
        }
    }
}
