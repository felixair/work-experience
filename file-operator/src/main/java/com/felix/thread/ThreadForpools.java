package com.felix.thread;

public class ThreadForpools implements Runnable {

    private Integer index;

    public  ThreadForpools(Integer index) {
        this.index = index;
    }
    public void run() {
        /***
         * 业务......省略
         */
        try {
            System.out.println("开始处理线程！！！" + index + " === ");
            Thread.sleep(index*100);
            System.out.println("我的线程标识是：" + index + " === " + this.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
