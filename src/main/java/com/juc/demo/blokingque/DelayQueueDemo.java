package com.juc.demo.blokingque;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DelayQueueDemo
 * @Description 延迟队列测试用例
 * @Author wangjian
 * @Date 2020/3/28 11:49 下午
 * @Version 1.0
 **/
public class DelayQueueDemo {
    public static void main(String[] args) {
        DelayQueue<Task> tasks = new DelayQueue<>();
        tasks.add(new Task("任务-A", 5L, TimeUnit.SECONDS));
        tasks.add(new Task("任务-B", 8L, TimeUnit.SECONDS));
        try {
            long start = System.currentTimeMillis();
            while (!tasks.isEmpty()) {
                Task take = tasks.take();
                System.out.println(take.toString());
            }
            System.out.println(System.currentTimeMillis() - start);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tasks.clear();
    }
}

class Task implements Delayed {

    // 任务名称
    private String name;
    // 在队列中的等待时长
    private long delay;
    // 准确的弹出时间
    private long expire;

    public Task(String name, Long delay, TimeUnit timeUnit) {
        this.name = name;
        this.delay = TimeUnit.MILLISECONDS.convert(delay, timeUnit); // 将等待时间转换为毫秒
        this.expire = System.currentTimeMillis() + this.delay; // 设置弹出的时间点
    }

    /*
    *
     * @Author wangjian
     * @Description  获取延迟队列内容弹出的剩余时间
     * @Date 11:51 下午 2020/3/28
     * @Param unit: 
     * @return long
     **/
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    /*
    *
     * @Author wangjian
     * @Description  查看是否到达弹出时间
     * @Date 11:53 下午 2020/3/28
     * @Param o: 
     * @return int
     **/
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.delay - this.getDelay(TimeUnit.MILLISECONDS)); // 等待时间减去剩余时间
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", delay=" + delay +
                ", expire=" + expire +
                '}';
    }
}
