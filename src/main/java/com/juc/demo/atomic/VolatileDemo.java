package com.juc.demo.atomic;


/**
 * @Auther: wangjian
 */
public class VolatileDemo {
    /**
     * volatile 为原子操作，并无同步的作用; 同时与 synchronized 没任何关系，synchronized 是控制线程同步，volatile 为属性的原子操作
     */

    public static void main(String[] args) {
        int num = 15000;
        ThreadTest threadTest = new ThreadTest(num);
        Thread a = new Thread(threadTest, "A");
        Thread b = new Thread(threadTest, "B");
        Thread c = new Thread(threadTest, "C");
        a.start();
        b.start();
        c.start();

    }

}

 class ThreadTest implements Runnable {

    private volatile int index;

    public ThreadTest(int index) {
        this.index = index;
    }

    @Override
    public void run() {
            while (sell()) {
                ;
            }
        System.out.println("A 执行了：" + Data.A);
        System.out.println("B 执行了：" + Data.B);
        System.out.println("C 执行了：" + Data.C);
    }

    private synchronized boolean sell() {
        try {
            if (index > 0) {
                index--;
                String name = Thread.currentThread().getName();
                if (name.equals("A")) {
                    Data.A++;
                } else if (name.equals("B")) {
                    Data.B++;
                } else if (name.equals("C")) {
                    Data.C++;
                }
//                TimeUnit.MILLISECONDS.sleep(2);
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

 class Data {
    static volatile int  A;
    static volatile int B;
    static volatile int C;
}
