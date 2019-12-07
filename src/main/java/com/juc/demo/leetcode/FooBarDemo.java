package com.juc.demo.leetcode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FooBarDemo {

  public static void main(String[] args) {
    FooBar fooBar = new FooBar(4);
    new Thread(()->{
      try {
        fooBar.foo(()->{
          System.out.println("foo");
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
    new Thread(()->{
      try {
        fooBar.bar(()->{
          System.out.println("bar");
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  static class FooBar {
    private int n;
    private Lock lock = new ReentrantLock();
    private Condition fooCondition = lock.newCondition();
    private Condition barCondition = lock.newCondition();
    private boolean bFlag = true;
    public FooBar(int n) {
      this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
      for (int i = 0; i < n; i++) {
        try {
          lock.lock();
          if (!bFlag) {
            fooCondition.await(); // 打印 foo 线程等待
          }
          printFoo.run();
          bFlag = false;
          // 释放控制
          barCondition.signal();
        } finally {
          lock.unlock();
        }
      }
    }

    public void bar(Runnable printBar) throws InterruptedException {

      for (int i = 0; i < n; i++) {
        try {
          lock.lock();
          if (bFlag) {
            barCondition.await();
          }
          printBar.run();
          bFlag = true;
          fooCondition.signal();
        } finally {
          lock.unlock();
        }
      }
    }
  }
}
