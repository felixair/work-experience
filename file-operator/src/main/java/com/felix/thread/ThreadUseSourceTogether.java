package com.felix.thread;

public class ThreadUseSourceTogether implements Runnable {
    private static int count = 10;

    @Override
    public void run() {

        while (count > 0) {
            try {
                Thread.sleep(23);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            synchronized (this) {
                if (count > 0) {
                    count--;
                    System.out.println(Thread.currentThread().getName() + "卖出一张票,票还剩" + count);
                }

            }

        }

    }

    public static void main(String[] args) {
        ThreadUseSourceTogether t = new ThreadUseSourceTogether();

        new Thread(t, "--a--").start();
        new Thread(t, "--b--").start();
        new Thread(t, "--c--").start();
    }

}
