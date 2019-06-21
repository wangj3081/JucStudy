package com.juc.demo.atomic;

import com.juc.demo.util.EntityUtil;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: wangjian
 */
public class AtomicReferenceDemo {

    /**
     * 通过原子类做 CAS 转换，由于 CAS 的实现是根据对象的引用地址来进行比较替换的，所以下例中只有将相同对象去跟原子对象中的对象去比较才能替换
     * @param args
     */
    public static void main(String[] args) {
        Member member = new Member("小明",22);
        AtomicReference<Member> reference = new AtomicReference<>(member); // 原子对象
        boolean b = reference.compareAndSet(new Member("小明", 22), new Member("小红", 18)); // 该修改不会生效
        System.out.println(b+"修改失败: " + reference.get().toString());
        boolean compareAndSet = reference.compareAndSet(member, new Member("小米", 33));
        System.out.println( compareAndSet+ "修改成功: " + reference.get().toString());

    }

    static class Member extends EntityUtil {
        private String name;

        private int age;

        public Member() {
        }

        public Member(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }
}
