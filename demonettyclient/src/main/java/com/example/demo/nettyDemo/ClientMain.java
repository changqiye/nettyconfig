package com.example.demo.nettyDemo;

import com.example.demo.ehcatchServer.EhCatchHelper;
import com.example.demo.utils.HttpUtils;
import com.example.demo.utils.TextLogUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Administrator on 2020/8/12.
 */
public class ClientMain {
    private static int port = 8280;
    private static ChannelFuture cf;
    private final static String url = "http://192.168.190.128:8183/Example/getServerAddress";

    static {
        System.err.println("static 代码块被执行！");
        new Thread(() -> {
            start();
        }).start();
    }

    public static void start() {
        System.err.println("开始启动netty客户端！");
        TextLogUtils.writeLog("开始启动netty客户端！");
        try {
            //指定分隔符
            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes("UTF-8"));
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            Bootstrap bs = new Bootstrap();
            bs.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ///自定义分隔符
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            // marshalling 序列化对象的解码
                            socketChannel.pipeline().addLast(new StringDecoder());
                            // marshalling 序列化对象的编码
                            socketChannel.pipeline().addLast(new StringEncoder());
                            // 处理来自服务端的响应信息
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            // 客户端开启
            //cf = bs.connect("localhost", port).sync();
            cf = bs.connect(getIp(), port).sync()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                TextLogUtils.writeLog("连接到远程服务器：" + cf.channel().remoteAddress() + getClientStatus());
                            }
                        }
                    });
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println(e);
        }
        if (cf.channel().isOpen()) {
            TextLogUtils.writeLog("客户端开启!");
        } else {
            TextLogUtils.writeLog("客户端关闭!");
        }
    }

    private static String getIp() {
        String ip = HttpUtils.get(url);
        if (!ip.startsWith("10")) {
            return getIp();
        } else {
            ip = ip.replaceAll("/r|/n", "");
            TextLogUtils.writeLog("服务端IP：" + ip);
            return ip.trim();
        }
    }

    public static String getClientStatus() {
        return "isActive=[" + cf.channel().isActive() + "]  isRegistered=[" + cf.channel().isRegistered() + "]  isOpen=[" + cf.channel().isOpen() + "]";
    }

    public static void main(String[] args) {
        EhCatchHelper.put("a", "b");
        System.err.println(EhCatchHelper.get("a"));
        System.err.println("start");
        new Thread(() -> {
            start();
        }).start();
        try {
            Thread.sleep(2000);
            if (cf != null) {
                sendMessageToServer("王牌飞行员前来报到！$_");
                getClientStatus();
            } else {
                System.err.println("无法获取channel");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static String sendMessageToServer(String message) {
        try {
            cf.channel().writeAndFlush(message).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    TextLogUtils.writeLog("消息[" + message + "]已发送至服务端：[" + cf.channel().remoteAddress() + "]");
                }
            });
        } catch (Exception e) {
            TextLogUtils.writeLog("向服务端发送消息异常：" + e.getMessage());
        }
        return "done";
    }
}
