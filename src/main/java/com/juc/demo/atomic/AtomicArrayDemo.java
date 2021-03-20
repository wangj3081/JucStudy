package com.juc.demo.atomic;

import java.util.concurrent.atomic.AtomicReferenceArray;


/**
 * @ClassName AtomicArrayDemo
 * @Description 数组原子类操作
 * @Author wangjian
 * @Date 2021/3/21 上午12:33
 * @Version 1.0
 **/
public class AtomicArrayDemo {

    public static void main(String[] args) {
        AtomicReferenceArray<String> array = new AtomicReferenceArray<String>(3);
        array.set(0, "admin");
        array.set(1, "customer");
        array.set(2, "other");
        // 变更失败, 因为这里的比较变量使用了new关键字，开辟了新的内存
        // 在CAS方法中比较使用的是地址的比较
//        String admin = new String("admin").intern(); // 写入常量池之后就可以替换
        String admin = new String("admin");
        array.compareAndSet(0, admin, "管理员");
        // 变更成功
//        String str = "admin";
//        array.compareAndSet(0, str, "管理员");
        // 查看两个对象的地址
//        System.out.println(Integer.toHexString(System.identityHashCode(admin)));
//        System.out.println(Integer.toHexString(System.identityHashCode(str)));
        System.out.println(array.get(0));
    }
}
