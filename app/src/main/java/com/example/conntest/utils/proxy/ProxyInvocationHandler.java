package com.example.conntest.utils.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler {

    private Object subject;

    private ProxyInterceptor interceptor;

    public ProxyInvocationHandler(Object subject, ProxyInterceptor interceptor) {
        this.subject = subject;
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        if (!interceptor.onIntercept(subject, method, args)){
            return new ProxyBulk(subject, method, args).safeInvoke();
        }
        return null;
    }
}
