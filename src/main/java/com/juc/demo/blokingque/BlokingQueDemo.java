package com.juc.demo.blokingque;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *  开两个线程从 1 打印到 100, 一个线程负责打印奇数、一个负责打印偶数,顺序执持
 *  通过 SynchronusQueue(同步阻塞队列,单值队列)的特性配合ReentrantLock(独占锁)就可以完成此目的
 * @Auther: wangjian
 */
public class BlokingQueDemo {

    private static Lock lock = new ReentrantLock();
  /*  private static  Condition oddCondition;
    private static  Condition evenCondition;
    static {
        oddCondition = lock.newCondition(); // 条件控制，需要与 lock 一起配合使用
        evenCondition = lock.newCondition();
    }*/
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> oddBlockingQueue = new SynchronousQueue<>();
        BlockingQueue<Integer> eventBlockingQueue = new SynchronousQueue<>();
        OddumberThread oddumberThread = new OddumberThread(oddBlockingQueue);
        EvenNumberThread evenNumberThread = new EvenNumberThread(eventBlockingQueue);
        new Thread(oddumberThread).start();
        new Thread(evenNumberThread).start();
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                eventBlockingQueue.put(i);
            } else {
                oddBlockingQueue.put(i);
            }
        }

    }

    static class  OddumberThread implements Runnable {
        BlockingQueue<Integer> blockingQueue = null;
        private OddumberThread() {

        }
        public OddumberThread(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }
        @Override
        public void run() {
            while (true) {
                Integer take = null;
                try {
                    lock.lock();
                    take = blockingQueue.poll();
                    if (take != null) {
//                        evenCondition.signal();
                        System.out.println("打印奇数:" + take);
//                        oddCondition.await();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }


    }

    static class EvenNumberThread implements Runnable {

        BlockingQueue<Integer> blockingQueue = null;
        private EvenNumberThread() { }

        public EvenNumberThread(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
                while (true) {
                    Integer take = null;
                    try {
                        lock.lock();
                        take = blockingQueue.poll();
                        if (take != null) {
//                            oddCondition.signal();
                            System.out.println("打印偶数:" + take);
//                            evenCondition.await();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }

        }
    }
}
