package com.juc.demo.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @ClassName ForkJoinPoolDemo
 * @Description TODO
 * @Author wangjian
 * @Date 2021/3/27 下午11:19
 * @Version 1.0
 **/
public class ForkJoinPoolDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool joinPool = new ForkJoinPool();
        SumTask sumTask = new SumTask(1, 100);
        joinPool.execute(sumTask);
        ForkJoinTask<Integer> submit = joinPool.submit(sumTask);
        System.out.println(submit.get());
        System.out.println(sumTask.get());
    }
}

/**
 * 分治计算
 */
class SumTask extends RecursiveTask<Integer> {

    private int start;

    private int end;



    public SumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int count = 0; // 每个任务计算的结果对象
        if (end - start <= 100) {
            for (int i = this.start; i <= end; i++) {
                count += i;
            }
        } else {
            int middle = (start + end) / 2;
            SumTask left = new SumTask(start, middle);
            SumTask right = new SumTask(middle + 1, end);
            left.fork();    // 开启分支操作，调用compute方法
            right.fork();   // 开启分支操作，调用compute方法
            return left.join() + right.join();
        }
        return count;
    }
}
