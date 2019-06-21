package com.juc.demo.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子形操作
 * @Auther: wangjian
 */
class Count{

    private AtomicInteger count = new AtomicInteger(0); // 原子型控制,保证数据安全操作

    private int index = 0;

    public void addOne() {
        count.addAndGet(1);
        this.index = index + 1;
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  int getCount() {
        return count.get();
    }

    public int getIndex() {
        return index;
    }

}
public class AtomicDemo {

    public static void main(String[] args) throws InterruptedException {
        Count count = new Count();
        for (int i = 0; i < 1000; i++) {
            new Thread(()-> {
                count.addOne();
            }).start();
        }
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("count:" + count.getCount());
        System.out.println("index:" + count.getIndex());
    }
}
