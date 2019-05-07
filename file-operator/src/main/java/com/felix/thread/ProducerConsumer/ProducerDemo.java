package com.felix.thread.ProducerConsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

//消费者
public class ProducerDemo implements Runnable {

    private final BlockingQueue<Integer> queue;

    public ProducerDemo(BlockingQueue q){
        this.queue = q;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);//模拟耗时
                queue.put(produce());
            }
        } catch (InterruptedException e) {

        }
    }

    private int produce() {
        int n = new Random().nextInt(10000);
        System.out.println("Thread:" + Thread.currentThread().getId() + "--" + Thread.currentThread().getName() + " produce:" + n);
        return n;
    }

}

