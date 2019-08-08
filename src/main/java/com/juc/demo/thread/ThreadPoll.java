package com.juc.demo.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by wangjian on 2019/8/8
 * @author wangjian
 * @version 1.0
 * @see ThreadPoll
 * @since JDK1.8
 */
public class ThreadPoll {

    public static void main(String[] args) throws Exception{
        CompletionServiceTest.completitonServiceTest();
        ForkJoinPoolTest.forkJoinPoolTest();
    }
}

/**
 *  CompletionService 线程池的使用，可以获取线程返回值的线程池
 */
class CompletionServiceTest {
    /**
     * 计算从 0 到 10 每个数值之间的累加
     */
    public static void completitonServiceTest() {
        // 创建处理的线程池
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("completionServiceThreadPoll").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 50, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), threadFactory);
        CompletionService  completionService = new ExecutorCompletionService(executor);
        AtomicInteger sum = new AtomicInteger();
        Lock lock = new ReentrantLock();
        for (int i = 0; i <= 10; i++) {
            int finalI = i;
            completionService.submit(() -> {
                try {
                    lock.lock();
                    Thread.currentThread().setName("completionServiceThreadPoll ->" + finalI);
                    sum.addAndGet(finalI);
                } finally {
                    lock.unlock();
                }
                // 线程池
                return Thread.currentThread().getName() + " ->" + sum.get();
            });

        }

        for (int i = 0; i <= 10; i++) {
            try {
                System.out.println(completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}

/**
 * 多分支任务处理
 */
class ForkJoinPoolTest {

    public static void forkJoinPoolTest() throws ExecutionException, InterruptedException {
        ForkJoinPool  joinPool = new ForkJoinPool();
        AddTask addTask = new AddTask(0,100);
        joinPool.submit(addTask);
        System.out.println(addTask.get());
        joinPool.shutdown();
    }

    static class AddTask extends RecursiveTask<Integer> {
        private Integer start;
        private Integer end;

        private AddTask() { }
        public AddTask(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        /**
         * 分支任务处理
         * @return
         */
        @Override
        protected Integer compute() {
            int sum = 0;
            if (end - start < 100) {
               /* if (start == 51) {
                    // 注意：如果在拆分的任务中执行抛出异常，会导致所有分支的任务执行失败、
                    // 不要做IO操作
                    throw new RuntimeException("程序异常了。。。。");
                }*/
                for (int x = start;x <= end; x++) {
                    sum += x;
                }
            } else {
                // 取中位数，拆分任务
                int middle = (start+end) / 2;
                AddTask leftJob = new AddTask(start, middle);
                AddTask rightJob = new AddTask(middle + 1, end);
                leftJob.fork(); // 开启下一个分支计算，开启的为 compute 方法
                rightJob.fork(); // 开启下一个分支计算，开启的为 compute 方法
                sum = leftJob.join() + rightJob.join(); // 合并两个计算后的结果

            }
            return sum;
        }
    }
}
