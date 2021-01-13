package com.example.demo.zk;

import com.example.demo.nettyDemo.utils.TextLogUtils;
import com.example.demo.nettyDemo.utils.ThreadUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Created by Administrator on 2020/10/21.
 */
@Component
public class ZKClientInfo  {
    @Value("${zk.server.address}")
    private String zkServerAddress;

    public CuratorFramework zkClient;

    @Value("${zk.server.path}")
    private String zkServerPathRoot;

    public void initZkClient() {
        TextLogUtils.writeLog("启动链接：" + zkServerAddress);
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServerAddress)
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        TextLogUtils.writeLog("启动状态：" + zkClient.getState());
        ///添加当前服务器ip
        addServerNode();
        zkWatch(zkServerPathRoot);
    }

    private void addServerNode() {
        try {
            String serverNodePath = String.format("%s/%s", zkServerPathRoot, InetAddress.getLocalHost().getHostAddress());
            TextLogUtils.writeLog("向ZK 中添加本服务临时节点 = [" + serverNodePath + "]");
            if (zkClient.checkExists().forPath(serverNodePath) == null) {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(serverNodePath, "".getBytes());
            }
        } catch (Exception e) {
            TextLogUtils.writeLog(e.getMessage());
        }
    }

    private void zkWatch(String path) {
        TextLogUtils.writeLog("开始监听节点：" + path);
        try {
            /*
             * NodeCache	监听节点对应增、删、改操作
             * PathChildrenCache	监听节点下一级子节点的增、删、改操作
             * TreeCache	可以将指定的路径节点作为根节点，对其所有的子节点操作进行监听，呈现树形目录的监听
             */
            TreeCache treeCache = new TreeCache(zkClient, path);
            treeCache.getListenable().addListener(new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    ChildData eventData = event.getData();
                    switch (event.getType()) {
                        case NODE_ADDED:
                            TextLogUtils.writeLog(path + "添加" + eventData.getPath() + "节点;数据为：" + new String(eventData.getData()));
                            break;
                        case NODE_UPDATED:
                            TextLogUtils.writeLog(eventData.getPath() + "节点数据更新;更新数据为：" + new String(eventData.getData()) + "\t版本为：" + eventData.getStat().getVersion());
                            break;
                        case NODE_REMOVED:
                            TextLogUtils.writeLog(eventData.getPath() + "节点被删除");
                            ///这里需要做校验机制，校验当前服务是否真的不可用！
                            addServerNode();
                            break;
                        default:
                            break;
                    }
                }
            });
            treeCache.start();
        } catch (Exception e) {
            TextLogUtils.writeLog(e.getMessage());
        }
    }
}
