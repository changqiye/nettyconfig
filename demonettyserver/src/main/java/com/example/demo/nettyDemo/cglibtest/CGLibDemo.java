package com.example.demo.nettyDemo.cglibtest;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.nettyDemo.utils.TextLogUtils;
import net.sf.cglib.reflect.FastClass;

import java.lang.reflect.Method;


public class CGLibDemo {

    public static void main(String[] args) {
        String aa = "a";

        new Thread(() -> {
            long index = 999999999;
            while (index > 0) {
                index--;
                testJDKReflect(aa);
            }
        }).start();

        ///  testCGlib(aa);
    }

    private static Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        return classType;
    }

    public static String testCGlib(Object... args) {
        try {
            ///CGLIB动态代理
//            TestCGlibMethodDemo myInterface = new TestCGlibMethodDemo();
//            MyMethodInterceptor myInvocationHandler = new MyMethodInterceptor(myInterface);
//            TestCGlibMethodDemo entity = (TestCGlibMethodDemo) myInvocationHandler.createCglibProxy();
//            entity.doSomething("aaa");


            TestCGlibMethodDemo serviceBean = new TestCGlibMethodDemo();
            if (serviceBean == null) {
                return null;
            }

            Class[] parameterTypes = new Class[args.length];
            // Get the right class type
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = getClassType(args[i]);
            }

            Class<?> serviceClass = serviceBean.getClass();
            String methodName = "doSomething";
            // Cglib reflect
            FastClass serviceFastClass = FastClass.create(serviceClass);
            // for higher-performance
            int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
            Object obj = serviceFastClass.invoke(methodIndex, serviceBean, args);
            TextLogUtils.writeLog(JSONObject.toJSONString(obj));
            if (obj != null) {
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }

    public static String testJDKReflect(Object... args) {
        try {
            TestCGlibMethodDemo serviceBean = new TestCGlibMethodDemo();
            if (serviceBean == null) {
                return null;
            }
            String aa = "aa";
            Class[] parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = getClassType(args[i]);
            }
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = "doSomething";
            // JDK reflect
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object obj = method.invoke(serviceBean, args);
            TextLogUtils.writeLog(JSONObject.toJSONString(obj));
            if (obj != null) {
                return "success";
            }
        } catch (Exception e) {
            TextLogUtils.writeLog(e.getMessage());
        }
        return "failed";
    }
}
