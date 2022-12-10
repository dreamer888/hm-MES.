package com.dream.iot.client.websocket;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.utils.UrlUtils;
import com.dream.iot.websocket.WebSocketException;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketScheme;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientConnectProperties extends ClientConnectProperties {

    private URI uri;
    private String subprotocol;
    private HttpHeaders customHeaders;
    private boolean allowMaskMismatch;
    private boolean performMasking = true;
    private boolean allowExtensions = false;
    private int maxFramePayloadLength = 65536;
    private long forceCloseTimeoutMillis = -1;
    private WebSocketVersion version = WebSocketVersion.V13;

    private WebSocketClientListener listener;

    protected WebSocketClientConnectProperties() { }

    /**
     * @param uri e.g ws://host:port/test
     */
    public WebSocketClientConnectProperties(String uri) {
        this(uri, null, null, UrlUtils.uriCutParam(uri));
    }

    /**
     *
     * @param scheme
     * @param remoteHost
     * @param remotePort
     * @param subUri /test or /test?a=1
     */
    public WebSocketClientConnectProperties(WebSocketScheme scheme, String remoteHost, Integer remotePort, String subUri) {
        this(scheme.name() + "://" + remoteHost + ":" + remotePort + UrlUtils.subUrlOfRootStart(subUri));
        this.setHost(remoteHost);
        this.setPort(remotePort);
    }

    /**
     * @param uri {@link #WebSocketClientConnectProperties(String)}
     * @param connectKey 用于支持同一个uri创建多个客户端
     */
    public WebSocketClientConnectProperties(String uri, String connectKey) {
       this(uri, null, null, connectKey);
    }

    public WebSocketClientConnectProperties(String uri, String localHost, Integer localPort, String connectKey) {
        this.setUri(uri);
        this.setConnectKey(connectKey);
        this.setLocalHost(localHost);
        this.setLocalPort(localPort);
    }

    /**
     * @param name {@link HttpHeaderNames}
     * @param value {@link HttpHeaderValues}
     * @return
     */
    public HttpHeaders addHeader(String name, Object value) {
        if(this.customHeaders == null) {
            this.customHeaders = new DefaultHttpHeaders();
        }

        this.customHeaders.add(name, value);
        return this.customHeaders;
    }

    public boolean isAllowExtensions() {
        return allowExtensions;
    }

    public void setAllowExtensions(boolean allowExtensions) {
        this.allowExtensions = allowExtensions;
    }

    public WebSocketVersion getVersion() {
        return version;
    }

    public void setVersion(WebSocketVersion version) {
        this.version = version;
    }

    public HttpHeaders getCustomHeaders() {
        return customHeaders;
    }

    public void setCustomHeaders(HttpHeaders customHeaders) {
        this.customHeaders = customHeaders;
    }

    public String getSubprotocol() {
        return subprotocol;
    }

    public void setSubprotocol(String subprotocol) {
        this.subprotocol = subprotocol;
    }

    public int getMaxFramePayloadLength() {
        return maxFramePayloadLength;
    }

    public void setMaxFramePayloadLength(int maxFramePayloadLength) {
        this.maxFramePayloadLength = maxFramePayloadLength;
    }

    public boolean isPerformMasking() {
        return performMasking;
    }

    public void setPerformMasking(boolean performMasking) {
        this.performMasking = performMasking;
    }

    public boolean isAllowMaskMismatch() {
        return allowMaskMismatch;
    }

    public void setAllowMaskMismatch(boolean allowMaskMismatch) {
        this.allowMaskMismatch = allowMaskMismatch;
    }

    public long getForceCloseTimeoutMillis() {
        return forceCloseTimeoutMillis;
    }

    public void setForceCloseTimeoutMillis(long forceCloseTimeoutMillis) {
        this.forceCloseTimeoutMillis = forceCloseTimeoutMillis;
    }

    public WebSocketClientListener getListener() {
        return listener;
    }

    /**
     * 为此客户端绑定监听
     * @param listener
     */
    public void bindListener(WebSocketClientListener listener) {
        this.listener = listener;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(String uri) {
        try {
            this.uri = new URI(uri);
            this.setHost(this.uri.getHost());
            this.setPort(this.uri.getPort());
        } catch (URISyntaxException e) {
            throw new WebSocketException("uri["+uri+"]解析失败");
        }
    }
}
