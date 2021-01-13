package com.example.demo.etcdUtils;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;

import java.util.List;

public class EtcdUtil {

    // etcd客户端链接
    private static Client client = null;
    private static String IPPORT = null;

//    static {
//        IPPORT = System.getProperty("IPPORT", "http://endpoints.etcd.qa.17usoft.com:4001");
//        getEtcdClient();
//    }


    public static void main(String[] args) {
//        IPPORT = System.getProperty("IPPORT", "http://endpoints.etcd.qa.17usoft.com:4001");
//        Client clienttmp = getEtcdClient();
//        clienttmp.getKVClient();
    }

    // 链接初始化
    public static Client getEtcdClient() {
        if (client == null) {
            synchronized (EtcdUtil.class) {
                client = Client.builder().lazyInitialization(false).endpoints(IPPORT).build();
            }
        }
        return client;
    }

    /**
     * 根据指定的配置名称获取对应的value
     *
     * @param key 配置项
     * @return
     * @throws Exception
     */
    public static String getEtcdValueByKey(String key) throws Exception {
        KeyValue kv = getEtcdKeyValueByKey(key);
        if (kv != null) {
            return kv.getValue().toStringUtf8();
        } else {
            return null;
        }
    }

    /**
     * 根据指定的配置名称获取对应的keyvalue
     *
     * @param key 配置项
     * @return
     * @throws Exception
     */
    public static KeyValue getEtcdKeyValueByKey(String key) throws Exception {
        List<KeyValue> kvs = client.getKVClient().get(ByteSequence.fromString(key)).get().getKvs();
        if (kvs.size() > 0) {
            return kvs.get(0);
        } else {
            return null;
        }
    }

    /**
     * 新增或者修改指定的配置
     *
     * @param key
     * @param value
     * @return
     */
    public static void putEtcdValueByKey(String key, String value) throws Exception {
        client.getKVClient().put(ByteSequence.fromString(key), ByteSequence.fromBytes(value.getBytes("utf-8")));
    }

    /**
     * 删除指定的配置
     *
     * @param key
     * @return
     */
    public static void deleteEtcdValueByKey(String key) {
        client.getKVClient().delete(ByteSequence.fromString(key));
    }

    // V3 api配置初始化和监听
    public static void init() {
        try {
            // 加载配置
            getConfig(client.getKVClient().get(ByteSequence.fromString("test")).get().getKvs());
            // 启动监听线程
            new Thread(() -> {
                // 对某一个配置进行监听
                Watch.Watcher watcher = client.getWatchClient().watch(ByteSequence.fromString("etcd_key"));
                try {
                    while (true) {
                        watcher.listen().getEvents().stream().forEach(watchEvent -> {
                            KeyValue kv = watchEvent.getKeyValue();
                            // 获取事件变化类型
                            System.out.println(watchEvent.getEventType());
                            // 获取发生变化的key
                            System.out.println(kv.getKey().toStringUtf8());
                            // 获取变化后的value
                            String afterChangeValue = kv.getValue().toStringUtf8();
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getConfig(List<KeyValue> kvs) {
        if (kvs.size() > 0) {
            String config = kvs.get(0).getValue().toStringUtf8();
            System.out.println("etcd 's config 's configValue is :" + config);
            return config;
        } else {
            return null;
        }
    }
}
