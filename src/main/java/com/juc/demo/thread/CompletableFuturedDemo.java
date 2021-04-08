package com.juc.demo.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName CompletableFuturedDemo
 * @Description TODO
 * @Author wangjian
 * @Date 2020/4/16 3:57 下午
 * @Version 1.0
 **/
public class CompletableFuturedDemo {

    public static void main(String[] args) throws InterruptedException {
//        callableDemo();
        CompletableFuture<String> future = new CompletableFuture<>();
        CountDownLatch latch = new CountDownLatch(20);
        AtomicInteger peoples = new AtomicInteger(10);
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "准备完毕");
                try {
                    String complete = future.get();
                    if ("fire".equals(complete)) {
                        int result = peoples.addAndGet(-1);
//                        int result = peoples.getAndDecrement();
                        if (result >= 0) {
                            System.out.println(Thread.currentThread().getName() + "发射完毕,击杀一名敌军");
                        } else {
                            System.out.println(Thread.currentThread().getName() + "发射完毕,未成功击杀,因敌军已全军覆没");
                        }
                    } else if ("cancel".equals(complete)) {
                        System.out.println(Thread.currentThread().getName() + "已取消发射");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }, "弓箭手->" + i).start();
            // 去除已准备的线程
            latch.countDown();
        }
        // 等待所有线层准备完毕
        latch.await();
        TimeUnit.SECONDS.sleep(1);
        future.complete("fire"); // 命令
    }

    /*
    *
     * @Author wangjian
     * @Description  callable 使用
     * @Date 4:02 下午 2020/4/16
     * @Param :
     * @return void
     **/
    private static void callableDemo() {
        ThreadCallable callable = new ThreadCallable();
        FutureTask<String> task = new FutureTask<String>(callable);
        try {
            task.run();
            String s = task.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

class ThreadCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        String result = "执行完 call 方法后的内容";
        return result;
    }
}
