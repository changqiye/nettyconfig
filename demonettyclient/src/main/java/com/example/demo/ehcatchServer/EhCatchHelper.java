package com.example.demo.ehcatchServer;

import com.alibaba.fastjson.JSON;
import com.example.demo.nettyDemo.ClientMain;
import org.ehcache.Cache;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2020/8/18.
 */
public class EhCatchHelper {
    private static Cache<String, String> catchClient;

    static {
        ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .offheap(10, MemoryUnit.MB)
                .build();
        CacheConfiguration<String, String> configuration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, String.class, resourcePools)
                .build();
        catchClient = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("cacher", configuration)
                .build(true)
                .getCache("cacher", String.class, String.class);
    }

    public static String get(String key) {

        return catchClient.get(key);
    }

    public static void put(String key, String value) {
        catchClient.put(key, value);
    }
}
