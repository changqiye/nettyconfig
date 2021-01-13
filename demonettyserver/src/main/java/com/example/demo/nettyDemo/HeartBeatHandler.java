package com.example.demo.nettyDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Administrator on 2020/11/14.
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventStr = "";
            switch (event.state()) {
                case ALL_IDLE:
                    eventStr = "读空闲";
                    break;
                case READER_IDLE:
                    eventStr = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventStr = "读写空闲";
                    break;
            }
            System.err.println("客户端：" + ctx.channel().remoteAddress() + " 超时事件：" + eventStr);
            ctx.channel().closeFuture();
        }
    }
}
