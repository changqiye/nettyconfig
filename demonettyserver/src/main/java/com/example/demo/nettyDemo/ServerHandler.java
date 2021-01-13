package com.example.demo.nettyDemo;

import com.alibaba.fastjson.JSON;
import com.example.demo.nettyDemo.utils.TextLogUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/8/12.
 */
@Component
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    public static ChannelGroup channelGroups = new DefaultChannelGroup("ChannelGroups", GlobalEventExecutor.INSTANCE);

    public static Map<String, Channel> clientChannelMap = new HashMap<String, Channel>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().read();
        channelGroups.add(ctx.channel());
        String address = ctx.channel().remoteAddress().toString();
        address = address.substring(1, address.length());
        clientChannelMap.put(address, ctx.channel());
        TextLogUtils.writeLog("remoteAddress:" + address);
        TextLogUtils.writeLog("localAddress:" + address);
        TextLogUtils.writeLog("当前channel Map:" + JSON.toJSONString(clientChannelMap));
    }

    // 读取数据
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.err.println("read 开始");
        try {
            TextLogUtils.writeLog("接收到来自：" + ctx.channel().remoteAddress() + " 的消息：" + msg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        TextLogUtils.writeLog("来自客户端[" + ctx.channel().remoteAddress() + "] 的消息处理完毕！");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        TextLogUtils.writeLog("客户端[" + ctx.channel().remoteAddress() + "]断开连接！");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        TextLogUtils.writeLog("exceptionCaught：" + cause.getMessage());
        ctx.close();
    }
}
