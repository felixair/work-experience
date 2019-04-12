package com.felix.queue;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueueDemo {

    public static void main(String[] args) {

        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        Executors.newCachedThreadPool();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3l, TimeUnit.DAYS, queue);
    }

}
