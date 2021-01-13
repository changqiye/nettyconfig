package com.example.demo.nettyDemo;

import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;
import org.omg.CORBA.PUBLIC_MEMBER;

public class TestTTClassLoader {
    static {
        System.out.println("加载....");
    }

    public static void main(String[] args) {
        System.out.println("load Log class successfully");
    }

    public static void testSay() {
        System.out.println("加载执行");
    }

    private String logId;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
