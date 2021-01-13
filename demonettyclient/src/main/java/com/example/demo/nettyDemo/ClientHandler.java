package com.example.demo.nettyDemo;

import com.alibaba.fastjson.JSON;
import com.example.demo.CatchValueEntity;
import com.example.demo.ehcatchServer.EhCatchHelper;
import com.example.demo.utils.TextLogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Administrator on 2020/8/12.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        TextLogUtils.writeLog("channelReadComplete!");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        TextLogUtils.writeLog("client exceptionCaught:" + cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        try {
            TextLogUtils.writeLog("来自服务端得消息：" + s);

//            CatchValueEntity entity = JSON.parseObject(s, CatchValueEntity.class);
//            if (entity != null) {
//                EhCatchHelper.put(entity.getKey(), entity.getValue());
//            }
        } finally {
            ReferenceCountUtil.release(s);
        }
    }
}