package com.example.demo.nettyDemo.miniDemo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2020/10/12.
 */
public class BufferDemo {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("123.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] text = "this is txt word!".getBytes();
        byteBuffer.put(text);
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileChannel.close();
        fileOutputStream.close();
    }
}
