package com.juc.demo.leetcode;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * 现在有两种线程，氧 oxygen 和氢 hydrogen，你的目标是组织这两种线程来产生水分子。
 * <p>
 * 存在一个屏障（barrier）使得每个线程必须等候直到一个完整水分子能够被产生出来。
 * <p>
 * 氢和氧线程会被分别给予 releaseHydrogen 和 releaseOxygen 方法来允许它们突破屏障。
 * <p>
 * 这些线程应该三三成组突破屏障并能立即组合产生一个水分子。
 * <p>
 * 你必须保证产生一个水分子所需线程的结合必须发生在下一个水分子产生之前。
 * <p>
 * 换句话说:
 * <p>
 * 如果一个氧线程到达屏障时没有氢线程到达，它必须等候直到两个氢线程到达。
 * 如果一个氢线程到达屏障时没有其它线程到达，它必须等候直到一个氧线程和另一个氢线程到达。
 * 书写满足这些限制条件的氢、氧线程同步代码。
 * <p>
 *  
 * <p>
 * 示例 1:
 * <p>
 * 输入: "HOH"
 * 输出: "HHO"
 * 解释: "HOH" 和 "OHH" 依然都是有效解。
 * 示例 2:
 * <p>
 * 输入: "OOHHHH"
 * 输出: "HHOHHO"
 * 解释: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" 和 "OHHOHH" 依然都是有效解。
 *  
 * <p>
 * 提示：
 * <p>
 * 输入字符串的总长将会是 3n, 1 ≤ n ≤ 50；
 * 输入字符串中的 “H” 总数将会是 2n 。
 * 输入字符串中的 “O” 总数将会是 n 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/building-h2o
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class H2oDemo {

    public static void main(String[] args) throws InterruptedException {
        H2O h2O = new H2O();
        for (int i = 1; i < 9; i++) {
//            h2O.hydrogen(()->{
//                System.out.println("执行一次氢线程");
//            });
            new Thread(() -> {
                try {
                    h2O.hydrogen(() -> {
                        System.out.println("H");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            if (i % 2 == 0) {
//                h2O.oxygen(() -> System.out.println("执行一次氧线程"));
                new Thread(() -> {
                    try {
                        h2O.oxygen(() -> {
                            System.out.println("O");
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    static class H2O {
        // 氢的信号量
        private Semaphore hSemaphore = new Semaphore(2);
        // 氧的信号量
        private Semaphore oSemaphore = new Semaphore(1);
        // 栅栏
        private CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        public H2O() {

        }

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
            // 线程获取信号量
            hSemaphore.acquire();
//            if (b) {
                try {
                    // 等待线程集数
                    cyclicBarrier.await();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // releaseHydrogen.run() outputs "H". Do not change or remove this line.
                releaseHydrogen.run();
                // 释放信号量
                hSemaphore.release();
//            } else {
//                System.out.println(Thread.currentThread().getName() +" :" +"H fail");
//            }
//            System.out.println("生成了H");
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            oSemaphore.acquire();
//            if (b) {
                try {
                    cyclicBarrier.await();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                // releaseOxygen.run() outputs "O". Do not change or remove this line.
                releaseOxygen.run();
                // 释放信号量
                oSemaphore.release();
//            System.out.println("生成了O");
//            } else {
//                System.out.println(Thread.currentThread().getName() +" :" +"O fail");
//            }
        }
    }
}
