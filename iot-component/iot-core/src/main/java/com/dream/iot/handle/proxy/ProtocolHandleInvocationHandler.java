package com.dream.iot.handle.proxy;

import com.dream.iot.ProtocolHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Executor;

public abstract class ProtocolHandleInvocationHandler implements InvocationHandler {

    private Object target;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public ProtocolHandleInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals(ProtocolHandle.method.getName())) {
            final Object invoke = method.invoke(target, args);
            if(invoke != null) {
                if(getTarget().filter(invoke, getProxyClass())) {
                    Executor executor = getTarget().executor(invoke, getProxyClass());
                    // 异步处理代理业务
                    if(executor != null) {
                        executor.execute(() -> {
                            try {
                                proxyHandle(invoke, proxy);
                            } catch (Exception e) {
                                exceptionHandle(invoke, e);
                            }
                        });

                        return invoke;
                    } else { // 同步执行
                        try {
                            return proxyHandle(invoke, proxy);
                        } catch (Throwable e) {
                            exceptionHandle(invoke, e);
                        }
                    }
                } else {
                    return invoke;
                }

                return invoke;
            }

            return null;
        }

        return method.invoke(target, args);
    }

    protected void exceptionHandle(Object invoke, Throwable e) {
        if(e instanceof UndeclaredThrowableException) {
            Throwable throwable = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
            logger.error("Handle代理执行 执行失败 - 代理类型：{}", getProxyClass().getSimpleName(), throwable.getCause());
            getTarget().exceptionHandle(invoke, throwable.getCause(), getProxyClass());
        } else {
            getTarget().exceptionHandle(invoke, e, getProxyClass());
            logger.error("Handle代理执行 执行失败 - 代理类型：{}", getProxyClass().getSimpleName(), e);
        }
    }

    protected abstract Class<? extends ProtocolHandleProxy> getProxyClass();

    protected abstract Object proxyHandle(Object value, Object proxy);

    public ProtocolHandleProxy getTarget() {
        return (ProtocolHandleProxy) target;
    }
}
