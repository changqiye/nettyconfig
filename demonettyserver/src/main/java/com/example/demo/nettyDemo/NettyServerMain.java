package com.example.demo.nettyDemo;

import com.example.demo.nettyDemo.utils.TextLogUtils;
import com.example.demo.nettyDemo.utils.ThreadUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/8/12.
 */
@Component
public class NettyServerMain {
    @Value("${netty.server.port}")
    public int port;
    public ChannelFuture cf = null;


    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            TextLogUtils.writeLog(" netty start 开始！");
            //指定分隔符
            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes("UTF-8"));
            // nio服务的启动类
            ServerBootstrap sbs = new ServerBootstrap();
            // 配置nio服务参数
            sbs.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 说明一个新的Channel如何接收进来的连接
                    .option(ChannelOption.SO_BACKLOG, 128) // tcp最大缓存链接个数
                    .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT) //Bytebuf 分配方式
                    .childOption(ChannelOption.AUTO_READ, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
                    // .handler(new LoggingHandler(LogLevel.INFO)) // 打印日志级别
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ///自定义分隔符
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            // 序列化对象
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            ///检查一个心跳(空闲检测)
                            socketChannel.pipeline().addLast(new IdleStateHandler(60, 60, 60, TimeUnit.SECONDS));
                            ///空闲状态检测handler
                            socketChannel.pipeline().addLast(new HeartBeatHandler());
//                            // 网络超时时间(超时后将关闭服务端)
//                            socketChannel.pipeline().addLast(new ReadTimeoutHandler(5));
                            // 处理接收到的请求
                            socketChannel.pipeline().addLast(new ServerHandler()); // 这里相当于过滤器，可以配置多个
                            socketChannel.config().setAllocator(UnpooledByteBufAllocator.DEFAULT);
                        }
                    });
            // 绑定端口，开始接受链接
            cf = sbs.bind(port).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println(e);
            TextLogUtils.writeLog(e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void checkStatus() {
        if (cf != null && cf.channel().isOpen()) {
            TextLogUtils.writeLog("服务端开启成功");
        } else {
            TextLogUtils.writeLog("服务端开启成功");
        }
        if (cf.channel().isRegistered()) {
            TextLogUtils.writeLog("服务端channel Registered");
        }
    }

    public void close() {
        cf.channel().close();
        if (cf != null && cf.channel().isOpen()) {
            TextLogUtils.writeLog("服务端开启成功");
        } else {
            TextLogUtils.writeLog("服务端已经被关闭");
        }
        if (cf.channel().isRegistered()) {
            TextLogUtils.writeLog("服务端channel Registered");
        } else {
            TextLogUtils.writeLog("服务端channel not Registered");
        }
    }
}
