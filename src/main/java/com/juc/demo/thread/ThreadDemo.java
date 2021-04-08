package com.juc.demo.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @ClassName ThreadDemo
 * @Description TODO
 * @Author wangjian
 * @Date 2021/1/24 下午11:12
 * @Version 1.0
 **/
public class ThreadDemo {

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("一个线程线程");
                /*try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }*/
//                int val = 1/0;
            });
            thread.start();
            thread.interrupt();
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(thread.getName()+"中断");
            }
        }

        FutureTask<Long> futureTask = new FutureTask<Long>(new CallTest());
        futureTask.run();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    static class CallTest implements Callable<Long> {

        @Override
        public Long call() throws Exception {

            return 10L;
        }
    }

}
