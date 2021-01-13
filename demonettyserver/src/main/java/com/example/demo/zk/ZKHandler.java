package com.example.demo.zk;

import com.example.demo.nettyDemo.utils.TextLogUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2020/10/21.
 */
@Component
public class ZKHandler {
    @Value("${zk.server.path}")
    private String zkServerPathRoot;

    @Autowired
    private ZKClientInfo zkClientInfo;

    private void zkChildWatch() throws Exception {
        /*
         * NodeCache	监听节点对应增、删、改操作
         * PathChildrenCache	监听节点下一级子节点的增、删、改操作
         * TreeCache	可以将指定的路径节点作为根节点，对其所有的子节点操作进行监听，呈现树形目录的监听
         */
        PathChildrenCache watcher = new PathChildrenCache(zkClientInfo.zkClient, zkServerPathRoot, true);
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        List<ChildData> childDataList = watcher.getCurrentData();
        System.out.println(childDataList.size());
        // 添加事件监听器
        watcher.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                // 通过判断event type的方式来实现不同事件的触发
                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {  // 子节点初始化时触发
                    TextLogUtils.writeLog("子节点初始化成功;");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {  // 添加子节点时触发
                    TextLogUtils.writeLog("子节点:" + event.getData().getPath() + " 添加成功;");
                    TextLogUtils.writeLog("该子节点的数据为:" + new String(event.getData().getData()));
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {  // 删除子节点时触发
                    TextLogUtils.writeLog("子节点：" + event.getData().getPath() + " 删除成功");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {  // 修改子节点数据时触发
                    TextLogUtils.writeLog("子节点：" + event.getData().getPath() + " 数据更新成功!");
                }
            }
        });
    }

    public List<String> getServerList() {
        try {
            return zkClientInfo.zkClient.getChildren().forPath(zkServerPathRoot);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public String addPath(String path, String data) {
        try {
            TextLogUtils.writeLog("path = [" + path + "]   data = [" + data + "]  status = [" + zkClientInfo.zkClient.getState().toString());
            if (zkClientInfo.zkClient.checkExists().forPath(path) == null) {
                zkClientInfo.zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
            }
            return zkClientInfo.zkClient.getData().forPath(path).toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
