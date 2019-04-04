package com.felix.queue;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class LinkedBlockingQueueDemo {

    public static void main(String[] args) {

        LinkedBlockingQueue<Map<String, String>> queue = new LinkedBlockingQueue<>();

        Executors.newCachedThreadPool();
    }

}
