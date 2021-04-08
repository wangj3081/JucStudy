package com.juc.demo.leetcode;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * 相同的一个 ZeroEvenOdd 类实例将会传递给三个不同的线程：
 * <p>
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 每个线程都有一个 printNumber 方法来输出一个整数。请修改给出的代码以输出整数序列 010203040506... ，其中序列的长度必须为 2n。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-zero-even-odd
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 示例：
 * 输入：n = 2
 * 输出："0102"
 * 说明：三条线程异步执行，其中一个调用 zero()，另一个线程调用 even()，最后一个线程调用odd()。正确的输出为 "0102"
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-zero-even-odd
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class ZeroEvenOddDemo {

    public static void main(String[] args) {
        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(13);
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
    }

}

class ZeroEvenOdd {

    private int n;

    private int x = 0;

    private Semaphore zero = new Semaphore(1);
    private Semaphore event = new Semaphore(0);
    private Semaphore odd = new Semaphore(0);

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printlnNum) throws InterruptedException {
        while (x < n) {
            zero.acquire();
            printlnNum.accept(0);
            x += 1;
            if (x % 2 == 0) {
                event.release();
            } else {
                odd.release();
            }
        }
    }

    public void even(IntConsumer printlnNum) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            event.acquire();
            printlnNum.accept(x);
            zero.release();
        }
    }

    public void odd(IntConsumer printlnNum) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            odd.acquire();
            printlnNum.accept(x);
            zero.release();
        }
    }
}


