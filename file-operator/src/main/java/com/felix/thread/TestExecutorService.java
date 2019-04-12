package com.felix.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestExecutorService {

    public static void main(String[] args) {

        fixedThread();

    }

    public static void fixedThread() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 4; i++) {
            final int index = i;
            executorService.execute(new ThreadForpools(index));
        }
    }


    public static void cacheThread() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            executorService.execute(new ThreadForpools(index));
        }
    }

}
