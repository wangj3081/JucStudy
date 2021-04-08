package com.juc.demo.leetcode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * @ClassName ZeroEventOddDemo2
 * @Description TODO
 * @Author wangjian
 * @Date 2021/4/5 下午9:38
 * @Version 1.0
 **/
public class ZeroEventOddDemo2 {

    public static void main(String[] args) throws InterruptedException {
        ZeroEvenOdd2 zeroEvenOdd = new ZeroEvenOdd2(3);
        new Thread(() -> {
            try {
                zeroEvenOdd.zero(value -> {
                    System.out.print(value + "\t");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                zeroEvenOdd.odd(value -> {
                    System.out.print(value + "\t");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                zeroEvenOdd.even(value -> {
                    System.out.print(value + "\t");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println();
//        TimeUnit.SECONDS.sleep(4);

    }
}

class ZeroEvenOdd2 {
    private int n;

    private Lock lock = new ReentrantLock();

    private AtomicInteger integer = null;

    private AtomicBoolean bFlag = new AtomicBoolean(true);

    private Condition zeroCondition = lock.newCondition();

    private Condition numCondition = lock.newCondition();


    public ZeroEvenOdd2(int n) {
        this.n = n;
        this.integer = new AtomicInteger(0);
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (integer.get() < n) {
            try {
                lock.lock();
                while (!bFlag.get()) {
                    zeroCondition.await();
                }
                printNumber.accept(0);
                bFlag.set(false);
                integer.set(integer.addAndGet(1));
                numCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            try {
                lock.lock();
                while (bFlag.get() || integer.get() % 2 != 0) {
                    numCondition.await();
                }
                printNumber.accept(integer.get());
                bFlag.set(true);
                zeroCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            try {
                lock.lock();
                while (bFlag.get() || integer.get() % 2 == 0) {
                    numCondition.await();
                }
                printNumber.accept(integer.get());
                bFlag.set(true);
                zeroCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
