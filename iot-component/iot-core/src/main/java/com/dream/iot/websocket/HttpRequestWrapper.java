package com.dream.iot.websocket;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpRequestWrapper implements HttpRequest {

    private String rawPath;
    private HttpRequest request;
    private WebSocketVersion version;
    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequestWrapper(HttpRequest request, WebSocketVersion version) {
        this.request = request;
        this.version = version;
        this.rawPath = request.uri();
        int indexOf = this.rawPath.indexOf("?");
        if(indexOf > 0) {
            this.rawPath = this.rawPath.substring(0, indexOf);
            String queryStr = this.request.uri().substring(indexOf + 1);
            this.queryParams = Arrays.stream(queryStr.split("&"))
                    .map(item -> item.split("="))
                    .collect(Collectors.toMap(item -> item[0], item -> item[1]));
        }
    }

    @Override
    public HttpMethod getMethod() {
        return request.getMethod();
    }

    @Override
    public HttpMethod method() {
        return request.method();
    }

    @Override
    public HttpRequest setMethod(HttpMethod method) {
        return request.setMethod(method);
    }

    @Override
    public String getUri() {
        return request.getUri();
    }

    @Override
    public String uri() {
        return request.uri();
    }

    @Override
    public HttpRequest setUri(String uri) {
        return request.setUri(uri);
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return request.getProtocolVersion();
    }

    @Override
    public HttpVersion protocolVersion() {
        return request.protocolVersion();
    }

    @Override
    public HttpRequest setProtocolVersion(HttpVersion version) {
        return request.setProtocolVersion(version);
    }

    @Override
    public HttpHeaders headers() {
        return request.headers();
    }

    @Override
    public DecoderResult getDecoderResult() {
        return request.getDecoderResult();
    }

    @Override
    public DecoderResult decoderResult() {
        return request.decoderResult();
    }

    @Override
    public void setDecoderResult(DecoderResult result) {
        request.setDecoderResult(result);
    }

    public WebSocketVersion getVersion() {
        return version;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public String getRawPath() {
        return rawPath;
    }

    public Optional<String> getQueryParam(String key) {
        return Optional.ofNullable(this.queryParams.get(key));
    }
}
