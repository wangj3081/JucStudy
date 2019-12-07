package com.juc.demo.leetcode;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 *
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 每个线程都有一个 printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-zero-even-odd
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 示例：
 * 输入：n = 2
 * 输出："0102"
 * 说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-zero-even-odd
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class ZeroEvenOddDemo {

  public static void main(String[] args) {
    ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(14);
    new Thread(()->{
      try {
        zeroEvenOdd.zero(value -> {
          System.out.print(value+"\t");
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(()->{
      try {
        zeroEvenOdd.odd(value -> {
          System.out.print(value+"\t");
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(()->{
      try {
        zeroEvenOdd.even(value -> {
          System.out.print(value+"\t");
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println();
  }


    static class ZeroEvenOdd {
      private int n;
      private Lock lock = new ReentrantLock();
      private Condition zeroCondition = lock.newCondition();
      private Condition evenCondition = lock.newCondition();
      private Condition oddCondition = lock.newCondition();
      private AtomicInteger lockNum = new AtomicInteger(0);
      private AtomicInteger printVal = new AtomicInteger(0); ;


      public ZeroEvenOdd(int n) {
        this.n = n;
      }

      // 只输出 0
      public void zero(IntConsumer printNumber) throws InterruptedException {
        while (printVal.get() < n) {
          try {
            lock.lock();
            if (lockNum.get() != 0) {
              zeroCondition.await();
            }

            printNumber.accept(0);
            int zeroVal = printVal.get();
            printVal.addAndGet(1);
            if (zeroVal % 2 == 0 && zeroVal != 0) {
              lockNum.set(2);
              evenCondition.signal();
            } else {
              lockNum.set(1);
              oddCondition.signal();
            }

          } finally {
            lock.unlock();
          }
        }
      }
      // 只输出偶数
      public void even(IntConsumer printNumber) throws InterruptedException {
        while (printVal.get() < n) {
          try {
            lock.lock();
            if (lockNum.get() != 2) {
              evenCondition.await();
            }
            int value = printVal.get();
            if (value % 2 == 0) {
              printNumber.accept(value);
              // 换执行0
              lockNum.set(0);
              zeroCondition.signal();
            }
          } finally {
            lock.unlock();
          }
        }

      }
      // 只输出奇数
      public void odd(IntConsumer printNumber) throws InterruptedException {
        while (printVal.get() < n) {
          try {
            lock.lock();
            if (lockNum.get() != 1) {
              oddCondition.await();
            }
            int value = printVal.get();
            if (value % 2 != 0) {
//              System.out.println(value);
              printNumber.accept(value);
              // 换执行偶数
              lockNum.set(0);
              zeroCondition.signal();
            }
          } finally {
            lock.unlock();
          }
        }
      }
    }
}
