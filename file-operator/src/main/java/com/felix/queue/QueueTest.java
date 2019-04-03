package com.felix.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class QueueTest {

    public static void main(String[] args) {
        QueueTest main = new QueueTest();
        Queue<Integer> buffer = new LinkedList<>();
        int maxSize = 3;
        new Thread(main.new Producer(buffer, maxSize), "Producer1").start();
        new Thread(main.new Consumer(buffer, maxSize), "Comsumer1").start();
        new Thread(main.new Consumer(buffer, maxSize), "Comsumer2").start();
    }

    class Producer implements Runnable {
        private Queue<Integer> queue;
        private int maxSize;

        Producer(Queue<Integer> queue, int maxSize) {
            this.queue = queue;
            this.maxSize = maxSize;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == maxSize) {
                        try {
                            System.out.println("Queue is full");
                            queue.wait();
                            System.out.println("-------in Producer while--------");
                            for (Integer integer : queue) {
                                System.out.print(integer + " ");
                            }
                            System.out.println("-------in Producer while--------");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("after while before try");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Random random = new Random();
                    int i = random.nextInt();
                    System.out.println(Thread.currentThread().getName() + " Producing value : " + i);
                    queue.add(i);
                    queue.notifyAll();
                }
            }
        }
    }

    class Consumer implements Runnable {
        private Queue<Integer> queue;
        private int maxSize;

        public Consumer(Queue<Integer> queue, int maxSize) {
            super();
            this.queue = queue;
            this.maxSize = maxSize;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            System.out.println("Queue is empty");
                            queue.wait();
                            System.out.println("-------in Consumer while--------");
                            for (Integer integer : queue) {
                                System.out.print(integer + " ");
                            }
                            System.out.println("-------in Consumer while--------");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " Consuming value : " + queue.remove());
                    queue.notifyAll();
                }
            }
        }
    }

}
