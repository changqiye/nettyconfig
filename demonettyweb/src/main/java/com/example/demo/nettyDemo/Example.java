package com.example.demo.nettyDemo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2020/8/11.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/Example")
public class Example {

    @Autowired
    private ClientMain clientMain;

    @RequestMapping("/start")
    String start() {
        // System.out.println(clientMain.getPort());
        new Thread(() -> {
            clientMain.start();
        }).start();

//        try {
//            Thread.sleep(5000);
//            ScheduledThreadPoolExecutor schedulePool = new ScheduledThreadPoolExecutor(1);
//            //作为一个周期任务提交,period 为1000ms
//            schedulePool.scheduleAtFixedRate(new Thread() {
//                @Override
//                public void run() {
//                    index++;
//                    if (serverHandler.channelGroups != null && serverHandler.channelGroups.size() > 0) {
//                        serverHandler.channelGroups.writeAndFlush("当前第" + index + "条心跳消息； end$_");
//                    } else {
//                        System.out.println("没有链接得客户端！");
//                    }
//                }
//            }, 1, 5, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            return e.getMessage();
//        }
        return "success";
    }

    @RequestMapping("/sendServerMessage")
    String sendServerMessage(@RequestParam("message") String message) {
        try {
            if (clientMain.cf != null) {
                clientMain.cf.channel().writeAndFlush(message + "$_").addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        System.out.println("sendClientMessage：向服务端发送消息完成！");
                    }
                });
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "向服务端发送消息：" + message;
    }
}