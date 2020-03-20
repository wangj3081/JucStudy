package com.juc.demo.cyclicBarrier;

import java.util.concurrent.*;

/**
 *
 * @ClassName CyclicBarrierDemo
 * @Description 循环屏障，当在线程中使用其await方法时，需要等到其线程到达了指定的屏障数量方能继续往下执行(除非设置等待超时时间)，
 *              在创建了CyclicBarrier对象时，也可以实现一个Runnable的接口的run方法从而在达到屏障数量时触发执行
 * @Author wangjian
 * @Date 2020/3/20 11:09 下午
 * @Version 1.0
 **/
public class CyclicBarrierDemo {

    public static void main(String[] args) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, ()-> {
            System.out.println("组队完成，开始执行任务吧");
        });
        CountDownLatch latch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    System.out.println(Thread.currentThread().getName() + "->进入等待");
                    TimeUnit.SECONDS.sleep(1); // 模拟延迟操作
//                    cyclicBarrier.await(100, TimeUnit.MILLISECONDS); // 等待达到屏障点
                    cyclicBarrier.await(); // 等待达到屏障点
                    System.err.println(Thread.currentThread().getName() + "执行了");
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "线程——"+i).start();
        }
        latch.await();
        System.out.println("所有线程执行完毕");
    }
}
