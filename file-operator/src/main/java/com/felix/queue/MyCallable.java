package com.felix.queue;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {

    private String waitTime;

    public MyCallable(String timeInMillis) {
        this.waitTime = timeInMillis;
    }

    public String call() throws Exception {
        //return the thread name executing this callable task
        return Thread.currentThread().getName();
    }

}
