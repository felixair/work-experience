package com.felix.thread;

public class StopThread implements Runnable {

    private int ticket = 10;

    @Override
    public void run() {
        while ( ticket > 0) {
            try {
                Thread.sleep(23);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            synchronized (this) {
                if ( ticket > 0 ) {
                    System.out.println("卖票：ticket : " + this.ticket-- + " Thread name : " + Thread.currentThread().getName());
                }
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        // 分別执行任务
        /*new Thread(new StopThread(), "---a---").start();
        new Thread(new StopThread(), "---b---").start();
        new Thread(new StopThread(), "---c---").start();*/

        StopThread stopThread = new StopThread();
        new Thread(stopThread, "--a--").start();
        new Thread(stopThread, "--b--").start();
        new Thread(stopThread, "--c--").start();

        Thread.sleep(5);
        Thread.interrupted();

    }

}
