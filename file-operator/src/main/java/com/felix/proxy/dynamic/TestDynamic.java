package com.felix.proxy.dynamic;

import java.lang.reflect.Proxy;

public class TestDynamic {

    public static void main(String[] args) {

        Class[] proxyInterface = new Class[]{IBusiness.class, IBusiness2.class};
        LogInvocationHandler handler = new LogInvocationHandler(new Business());
        ClassLoader classLoader = Business.class.getClassLoader();
        IBusiness2 proxyBusiness = (IBusiness2) Proxy.newProxyInstance(classLoader, proxyInterface, handler);
        proxyBusiness.doSomeThing2();
        ((IBusiness)proxyBusiness).doSomeThing();

    }

}
