package com.juc.demo.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ReadWriteLockDemo
 * @Description ReadWriteLock 样例
 * @Author wangjian
 * @Date 2021/3/21 下午6:50
 * @Version 1.0
 **/
public class ReadWriteLockDemo {

    public static void main(String[] args) {
        Account account = new Account(0.0, "小明");
        double[] moneys = new double[]{100, 20, 30, 22, 50};
        for (int i = 0; i < moneys.length; i++) {
            int finalI = i;
            new Thread(() -> {
                account.save(moneys[finalI]);
            }, "存钱-" + i).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                while (true) {
                    account.read();
                }
            }, "查款-" + i).start();

        }
    }

    /**
     * 读锁是可多个线程一起操作，写锁只能有一个线程操作，读锁与写锁互斥
     */
    static class Account {

        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        private double salary;

        private String name;

        public Account(double salary, String name) {
            this.salary = salary;
            this.name = name;
        }

        void save(double money) {
            try {
                lock.writeLock().lock();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.salary += money;
                System.out.println(Thread.currentThread().getName() + "给" + this.name + "加了" + money + "元");
            } finally {
                lock.writeLock().unlock();
            }
        }


        void read() {
            try {
                lock.readLock().lock();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this.name + "当前薪水:" + this.salary);
            } finally {
                lock.readLock().unlock();
            }
        }

    }
}
