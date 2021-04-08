package com.juc.demo.atomic;

import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @ClassName AccumulatorDemo
 * @Description 并发安全计算Demo
 * {@link DoubleAccumulator} {@link LongAccumulator}
 * {@link DoubleAdder} {@link LongAdder}
 * @Author wangjian
 * @Date 2021/3/21 下午12:58
 * @Version 1.0
 **/
public class AccumulatorDemo {

    public static void main(String[] args) {
//        doubleAccumulatorTest();
        doubleAdderTest();

    }

    private static void doubleAdderTest() {
        // 并发加法器计算
        DoubleAdder doubleAdder = new DoubleAdder();
        doubleAdder.add(10);
        doubleAdder.add(20);
        doubleAdder.add(30);
        System.out.println(doubleAdder.sum());
    }

    private static void doubleAccumulatorTest() {
        // 设置计算方式与初始值
        DoubleAccumulator doubleAccumulator = new DoubleAccumulator((x, y)->(x+y)/2, 2);
        DoubleAccumulator doubleAccumulator2 = new DoubleAccumulator((x,y)->(x+y)/2, 2);
        doubleAccumulator.accumulate(100);
        doubleAccumulator2.accumulate(100);
        System.out.println(doubleAccumulator.doubleValue());
        System.out.println(doubleAccumulator2.doubleValue());
    }
}
