package com.example.conntest.utils.proxy;

import java.lang.reflect.Proxy;

public class ProxyUtils {
    public static <T> T getProxy(Object object, Class<?>[] intfs, ProxyInterceptor interceptor, boolean weakRef, boolean postUI) {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
                intfs, new ProxyInvocationHandler(object, interceptor));
    }

    public static <T> T getProxy(Object object, Class<?> clazz, ProxyInterceptor interceptor, boolean weakRef, boolean postUI) {
        return getProxy(object, new Class<?>[] { clazz }, interceptor, weakRef, postUI);
    }

    public static <T> T getProxy(Object object, Class<?> clazz, ProxyInterceptor interceptor) {
        return (T) getProxy(object, clazz, interceptor, false, false);
    }
}
