package com.juc.demo.atomic;

import com.juc.demo.util.EntityUtil;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 原子形属性关键字
 * @Auther: wangjian
 */
class Member extends EntityUtil {

    private volatile String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        AtomicReferenceFieldUpdater fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(Member.class,String.class,"name");
        fieldUpdater.compareAndSet(this, this.name, name); // 使用该方法修改属性值，该属性必须为 volatile 关键字修饰的
    }
}
public class AtomicDemo {

    public static void main(String[] args) throws InterruptedException {
        Member member = new Member();
        member.setName("小明");
        System.out.println(member.toString());
        member.setName("小红");
        System.out.println(member.toString());
    }
}
