package com.example.conntest.utils.proxy;

import java.lang.reflect.Method;

public class ProxyBulk {
    private Method method;
    private Object object;
    private Object[] args;

    public ProxyBulk(Object object, Method method, Object[] args) {
        this.method = method;
        this.object = object;
        this.args = args;
    }

    public Object safeInvoke() {
        Object result = null;
        try {
            result = method.invoke(object, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object safeInvoke(ProxyBulk obj) {
        return obj.safeInvoke();
    }
}
