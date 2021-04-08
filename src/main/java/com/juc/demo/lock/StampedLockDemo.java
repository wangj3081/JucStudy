package com.juc.demo.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @ClassName StampedLockDemo
 * @Description ReadWriteLock的优化，当读线程多余写线程时使用
 * @Author wangjian
 * @Date 2021/3/21 下午6:50
 * @Version 1.0
 **/
public class StampedLockDemo {

    public static void main(String[] args) {
        Account account = new Account(0.0, "小明");
        double[] moneys = new double[]{100, 20, 30, 22, 50};
        for (int i = 0; i < moneys.length; i++) {
            int finalI = i;
            new Thread(() -> {
                account.save(moneys[finalI]);
            }, "存钱-" + i).start();
        }

        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                while (true) {
                    account.read();
                }
            }, "查款-" + i).start();

        }
    }

    /**
     * 无障碍锁
     */
    static class Account {

        private StampedLock lock = new StampedLock();

        private double salary;

        private String name;

        public Account(double salary, String name) {
            this.salary = salary;
            this.name = name;
        }

        void save(double money) {
            long lockStatus = lock.readLock(); // 获取读锁状态
            boolean bFlag = true;
            try {
                long writeLockState = lock.tryConvertToWriteLock(lockStatus); // 将当前锁转换为写锁
                while (bFlag) {
                    if (writeLockState != 0) {
                        lockStatus = writeLockState; // 最终解锁需要
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.salary += money;
                        System.out.println(Thread.currentThread().getName() + "给" + this.name + "加了" + money + "元");
                        bFlag = false;
                    } else {
                        this.lock.unlockRead(lockStatus); // 解除读锁
                        writeLockState = this.lock.writeLock(); // 获取写锁
                        lockStatus = writeLockState; // 最终解锁需要
                    }
                }
            } finally {
                this.lock.unlock(lockStatus); // 解锁
            }
        }


        void read() {
            long lockStatus = lock.tryOptimisticRead(); // 获取乐观锁
            try {
                double current = this.salary; // 获取当前的资产
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!this.lock.validate(lockStatus) || // 验证没通过
                        ((lockStatus & 1L << 7)) == 0L) { // 此段为获取乐观锁时要的一个判断
                    current = this.salary; //
                    lockStatus = this.lock.readLock();// 获取读锁
                }
                System.out.println(this.name + "当前薪水:" + current);
            } finally {
                lock.unlockRead(lockStatus);
            }
        }

    }
}
