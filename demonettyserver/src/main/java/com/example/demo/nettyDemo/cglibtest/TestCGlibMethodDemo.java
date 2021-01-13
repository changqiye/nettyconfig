package com.example.demo.nettyDemo.cglibtest;

import java.util.Random;

public class TestCGlibMethodDemo {
    public String doSomething(String request) {
        try {
            System.out.println("方法被调用");
            Thread.sleep(new Random().nextInt(100));
            return "你输入了：" + request;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
