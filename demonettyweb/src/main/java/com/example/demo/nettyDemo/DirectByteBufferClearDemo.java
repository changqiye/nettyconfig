package com.example.demo.nettyDemo;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2020/8/20.
 */
public class DirectByteBufferClearDemo {
    public static void clean(final ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            ((DirectBuffer) byteBuffer).cleaner().clean();
        }
    }

    public static void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {
        ///Direct 直接内存
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(102400);
        //ByteBuffer buffer = ByteBuffer.allocate(512);
        ///   buffer.clear(); 不会回收内存，只是刷新
        // buffer.clear();
        System.out.println("start");
        sleep(10000);
        // buffer.clear();
        clean(directBuffer);
        System.out.println("end");

    }
}
