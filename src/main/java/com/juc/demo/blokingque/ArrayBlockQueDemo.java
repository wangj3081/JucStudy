package com.juc.demo.blokingque;

import java.util.concurrent.*;

/**
 *
 */
public class ArrayBlockQueDemo {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(2);
        new Thread(() -> {
            while (true) {
                try {
                    // 使用 ArrayBlockingQueue 当队列没数据，或者队列满了，此时该线程会阻塞住
                    TimeUnit.SECONDS.sleep(2);
                    String take = blockingQueue.take();
                    System.out.println("获取到内容拉拉拉: " + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(()->{
                String str = ""+ finalI;
                try {
                    blockingQueue.put(str);
                    if (finalI == 2) {
                        System.out.println("队列中的数据未处理，不会执行这段");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
