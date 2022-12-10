package com.dream.iot.client.http;

public interface HttpManager {

    HttpResponseMessage get(HttpRequestMessage requestMessage);

    void get(HttpRequestMessage requestMessage, ClientHttpProtocol.HttpProtocolCall handle);

    HttpResponseMessage post(HttpRequestMessage requestMessage);

    void post(HttpRequestMessage requestMessage, ClientHttpProtocol.HttpProtocolCall handle);

}
