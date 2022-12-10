package com.dream.iot.taos.proxy;

import com.dream.iot.handle.proxy.ProtocolHandleInvocationHandler;
import com.dream.iot.handle.proxy.ProtocolHandleProxy;
import com.dream.iot.handle.proxy.ProtocolHandleProxyMatcher;
import com.dream.iot.taos.TaosHandle;
import com.dream.iot.taos.TaosSqlManager;

public class TaosHandleProxyMatcher implements ProtocolHandleProxyMatcher {

    private final TaosSqlManager taosSqlManager;

    public TaosHandleProxyMatcher(TaosSqlManager taosSqlManager) {
        this.taosSqlManager = taosSqlManager;
    }

    @Override
    public boolean matcher(Object target) {
        return target instanceof TaosHandle;
    }

    @Override
    public ProtocolHandleInvocationHandler invocationHandler(Object target) {
        return new ProtocolHandleInvocationHandler(target) {

            @Override
            protected Class<? extends ProtocolHandleProxy> getProxyClass() {
                return TaosHandle.class;
            }

            @Override
            protected Object proxyHandle(Object value, Object proxy) {
                taosSqlManager.execute(value, getTarget());
                return value;
            }

            @Override
            public TaosHandle getTarget() {
                return (TaosHandle) super.getTarget();
            }
        };
    }

    public TaosSqlManager getTaosSqlManager() {
        return taosSqlManager;
    }
}
