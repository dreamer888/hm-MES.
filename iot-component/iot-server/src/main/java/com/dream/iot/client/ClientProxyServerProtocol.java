package com.dream.iot.client;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolHandle;
import com.dream.iot.proxy.*;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;

/**
 * 代理应用客户端请求设备或者第三方客户端
 * 用来接收应用程序客户端请求
 * @see ClientProxyServerHandle
 */
public class ClientProxyServerProtocol extends ClientInitiativeProtocol<ProxyServerMessage> {

    private String msg;
    private Exception failEx;
    private ExecStatus execStatus;

    public ClientProxyServerProtocol(ProxyServerMessage requestMessage) {
       super(requestMessage);
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        if(business == null) {
            throw new ProtocolException("对象: " + ClientProxyServerHandle.class.getSimpleName()+"没有注入SpringBeanFactory");
        } else {
            business.handle(this);
        }

        return buildResponseMessage();
    }

    @Override
    public ClientInitiativeProtocol buildResponseMessage() {
        /**
         * 由此方法直接响应客户端 proxyClientHandle
         * @see com.dream.iot.server.protocol.ServerInitiativeProtocol#proxyClientHandle(Object)
         */
        if(responseMessage() != null) { // 此处会出现响应报文不为空的情况
            return null;
        } else {
            this.responseMessage = doBuildResponseMessage();
            if(this.responseMessage != null) {
                return this;
            } else {
                return null;
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    protected ProxyServerMessage doBuildResponseMessage() {

        ProxyServerMessage responseMessage;
        if(this.getExecStatus() == null) {
            return null;
        } else {
            /**
             * @see ClientProxyServerHandle#handle(ClientProxyServerProtocol)
             */
            ProxyClientResponseBody responseBody;
            if(getExecStatus() == ExecStatus.success) {
                this.msg = this.msg == null ? "success" : this.msg;
            } else if(getFailEx() != null){
                Throwable cause = getFailEx().getCause();
                if(cause != null) {
                    this.msg = cause.getMessage();
                } else {
                    this.msg = getFailEx().getMessage();
                }
            }

            responseBody = new ProxyClientResponseBody(this.msg, getExecStatus());
            responseMessage = new ProxyServerMessage(requestMessage().getHead(), responseBody);
            return ProxyClientMessageUtil.encoder(responseMessage);
        }
    }

    /**
     * @see ProxyServerMessage#doBuild(byte[])
     * @param requestMessage
     */
    @Override
    protected void doBuildRequestMessage(ProxyServerMessage requestMessage) { }

    @Override
    public ProxyClientType protocolType() {
        return ProxyClientType.Proxy_Client_Server;
    }

    public Exception getFailEx() {
        return failEx;
    }

    public ClientProxyServerProtocol setFailEx(Exception failEx) {
        this.failEx = failEx;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ClientProxyServerProtocol setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ExecStatus getExecStatus() {
        return execStatus;
    }

    public ClientProxyServerProtocol setExecStatus(ExecStatus execStatus) {
        this.execStatus = execStatus;
        return this;
    }
}
