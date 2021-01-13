package com.example.demo.nettyDemo;

import com.alibaba.fastjson.JSON;
import com.example.demo.nettyDemo.entity.CatchValueEntity;
import com.example.demo.nettyDemo.utils.TextLogUtils;
import com.example.demo.nettyDemo.utils.ThreadUtils;
import com.example.demo.zk.ZKClientInfo;
import com.example.demo.zk.ZKHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/8/11.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/Example")
public class Example {
    @Autowired
    private ServerHandler serverHandler;

    @Autowired
    private NettyServerMain nettyServerMain;

    @Autowired
    private ZKClientInfo zkClientInfo;

    @Autowired
    private ZKHandler zkHandler;

    @RequestMapping("/getIp")
    public String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @RequestMapping("/initZkClient")
    public void initZkClient() {
        zkClientInfo.initZkClient();
    }

    @RequestMapping("/closeNettyServer")
    public void closeNettyServer() {
        nettyServerMain.close();
    }


    @RequestMapping("/startNettyServer")
    public void startNettyServer() {
        nettyServerMain.start();
    }


    @RequestMapping("/addPath")
    public String addPath(String path, String data) {
        return zkHandler.addPath(path, data);
    }

    @RequestMapping("/getServerList")
    public String getServerList() {
        return JSON.toJSONString(zkHandler.getServerList());
    }

    @RequestMapping("/getServerAddress")
    public String getServerAddress() {
        List<String> address = zkHandler.getServerList();
        if (!address.isEmpty()) {
            Random rm = new Random();
            return address.get(rm.nextInt(address.size()));
        } else {
            return "error";
        }
    }

    @RequestMapping("/getClientChannelList")
    public String getClientChannelList() {
        if (serverHandler.clientChannelMap.isEmpty()) {
            return "channel map is Empty!";
        } else {
            return JSON.toJSONString(serverHandler.clientChannelMap);
        }
    }

    @RequestMapping("/sendClientMessage")
    public String sendClientMessage(@RequestParam("client") String client, @RequestParam("message") String message) {
        try {
            Channel clientChannel = serverHandler.clientChannelMap.get(client);
            if (clientChannel != null) {
                clientChannel.writeAndFlush(message + "$_").addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        TextLogUtils.writeLog("向客户端：" + client + " 发送消息完成！");
                    }
                });
            }
        } catch (Exception e) {
            TextLogUtils.writeLog(e.getMessage());
            return e.getMessage();
        }
        return "向客户端：" + client + " 发送消息完成!";
    }

    @RequestMapping("/setValue")
    public String setValue(@RequestParam("key") String key, @RequestParam("value") String value) {
        CatchValueEntity entity = new CatchValueEntity();
        entity.setValue(value);
        entity.setKey(key);
        serverHandler.channelGroups.writeAndFlush(JSON.toJSONString(entity) + "$_");
        return "done";
    }


    @RequestMapping("/sendJobClientMessage")
    public String sendJobClientMessage(@RequestParam("message") String message) {
        try {
            ScheduledThreadPoolExecutor schedulePool = new ScheduledThreadPoolExecutor(1);
            //作为一个周期任务提交,period 为1000ms
            schedulePool.scheduleAtFixedRate(new Thread() {
                @Override
                public void run() {
                    if (serverHandler.channelGroups != null && serverHandler.channelGroups.size() > 0) {
                        serverHandler.channelGroups.writeAndFlush(message + "$_");

//                                .addListener(new ChannelFutureListener() {
//                            @Override
//                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                                TextLogUtils.writeLog("向多个客户端发送轮询消息完成！");
//                            }
//                        });
                    } else {
                        TextLogUtils.writeLog("没有链接得客户端！");
                    }
                }
            }, 1, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "向客户端发送循环消息：" + message;
    }
}