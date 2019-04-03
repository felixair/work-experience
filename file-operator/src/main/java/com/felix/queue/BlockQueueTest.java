package com.felix.queue;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueueTest {

    public static void main(String[] args) throws Exception {

        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
        linkedBlockingQueue.put("aaa");
        linkedBlockingQueue.put("bbb");
        linkedBlockingQueue.put("ccc");
        linkedBlockingQueue.put("ddd");

        System.out.println(linkedBlockingQueue.poll());
        System.out.println(linkedBlockingQueue.poll());
        System.out.println(linkedBlockingQueue.poll());
        System.out.println(linkedBlockingQueue.poll());
        System.out.println(linkedBlockingQueue.size());

    }

}
