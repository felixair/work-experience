package com.felix.thread.ProducerConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestDemo {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(100);
        ProducerDemo p = new ProducerDemo(queue);
        ConsumerDemo c1 = new ConsumerDemo(queue);
        ConsumerDemo c2 = new ConsumerDemo(queue);

        new Thread(p, "Producer Name").start();
        new Thread(c1, "Consumer Name1").start();
        new Thread(c2, "Consumer Name2").start();

    }

}
