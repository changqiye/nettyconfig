package com.example.demo.nettyDemo.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    ///线程限制
    private static final int QueueCount = 500;
    private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(QueueCount);
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 6, 10, TimeUnit.SECONDS, queue);

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
