package com.juc.demo.thread;

/**
 * 普通多线程方法，做多线程加减计算，50 个线程加 1，50 个线程减 1，结果应该永远只在 -1,0 或者 0,1 之间相互转换
 */
public class ThreadPulsSub {

    public static void main(String[] args) {
        Resource resource = new Resource();
        new Thread(new AddThread(resource), "加线程").start();
        new Thread(new SubThread(resource), "减线程").start();
    }
}

class Resource {

    private int num = 0;
    private boolean bFlag = false; // true 为做加，false 为做减

    public synchronized void addNum() throws InterruptedException {
        if (!bFlag) { // 当前做减线程
            super.wait(); // 线程等待
        }
        System.out.println(Thread.currentThread().getName() + " : " + this.num++);
        bFlag = false;
        super.notifyAll(); // 唤醒所有线程
    }

    public synchronized void subNum() throws InterruptedException {
        if (bFlag) { // 当前做加线程
            super.wait();
        }
        System.out.println(Thread.currentThread().getName() + " : " + this.num--);
        bFlag = true;
        super.notifyAll(); // 唤醒所有线程
    }

}

class AddThread implements Runnable{

    private Resource resource = null;

    public AddThread(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                resource.addNum();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class SubThread implements Runnable{

    private Resource resource = null;

    public SubThread(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                resource.subNum();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}