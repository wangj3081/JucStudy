package com.juc.demo.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过 JUC 包完成多线程加减计算，50 个线程加 1，50 个线程减 1，结果应该永远只在 -1,0 或者 0,1 之间相互转换
 */
public class JucThreadPulsSub {

    public static void main(String[] args) {
        JucResource resource = new JucResource();
        new Thread(()->{
            for (int i = 0; i < 50; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resource.addNum();
            }
        },"加线程").start();

        new Thread(()->{
            for (int i = 0; i < 50; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resource.subNum();
            }
        }, "减线程").start();
    }
}

class JucResource {

    private int num = 0;
    private boolean bFlag = false;
    private Lock lock = new ReentrantLock();
    private Condition addCondition = lock.newCondition(); // 加线程控制条件
    private Condition subCondition = lock.newCondition(); // 减线程控制条件

    public void addNum() {
        try {
            lock.lock();
            if (!bFlag) { // 当前只能减线程做操作
                addCondition.await(); // 加线程等待
            }
            System.out.println(Thread.currentThread().getName() + " : " + this.num ++);
            bFlag = false;
            subCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void subNum() {
        try {
            lock.lock();
            if (bFlag) { // 当前只能加线程做操作
                subCondition.await(); // 加线程等待
            }
            System.out.println(Thread.currentThread().getName() + " : " + this.num --);
            bFlag = true;
            addCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }



}
