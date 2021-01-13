package com.example.demo.nettyDemo.cglibtest;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {

    private Object delegate;

    public MyMethodInterceptor(Object delegate) {
        this.delegate = delegate;
    }

    public Object createCglibProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(delegate.getClass());
        enhancer.setInterfaces(delegate.getClass().getInterfaces());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        Object result = method.invoke(delegate, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("cglib proxy: before method");
    }

    private void after() {
        System.out.println("cglib proxy: after method");
    }
}
