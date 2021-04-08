package com.juc.demo.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CompletableFuturedDemo2
 * @Description TODO
 * @Author wangjian
 * @Date 2021/3/22 下午10:57
 * @Version 1.0
 **/
public class CompletableFutureDemo {

    public static void main(String[] args) {
        CompletableFuture<String> future = new CompletableFuture<>();

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                try {
                    System.out.println(Thread.currentThread().getName()+":等待等待等待。。。");
                    // 此时会阻塞该线程
                    String val = future.get();
                    System.out.println(Thread.currentThread().getName()+" : "+ val);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }, "线程->" + i).start();
        }
        try {
            TimeUnit.SECONDS.sleep(2);
            future.complete("哈哈哈哈");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
