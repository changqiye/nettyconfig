package com.example.demo.nettyDemo;

import com.example.demo.utils.HttpUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2020/8/12.
 */
@Component
public class ClientMain implements CommandLineRunner {
    @Value("${netty.server.port}")
    private int port;

    public static ChannelFuture cf;

    // 请求端主题
    public void start() {
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
            cf = bs.connect("localhost", port).sync();
            System.out.println(cf.channel().remoteAddress());
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println(e);
        }
        if (cf.channel().isOpen()) {
            System.out.println("客户端开启!");
        } else {
            System.out.println("客户端关闭!");
        }
    }

    private String getIp() {
        String ip = HttpUtils.get("http://10.181.137.152:8183/Example/getServerAddress");
        if (!ip.startsWith("10")) {
            return getIp();
        } else {
            ip= ip.replaceAll("/r|/n", "");
            System.out.println(ip);
            return ip.trim();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.start();
    }
}
