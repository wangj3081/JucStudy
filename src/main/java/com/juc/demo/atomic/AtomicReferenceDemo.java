package com.juc.demo.atomic;

import com.juc.demo.util.EntityUtil;

import java.awt.dnd.DragSource;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Auther: wangjian
 */
public class AtomicReferenceDemo {

    /**
     * 通过原子类做 CAS 转换，由于 CAS 的实现是根据对象的引用地址来进行比较替换的，所以下例中只有将相同对象去跟原子对象中的对象去比较才能替换
     * @param args
     */
    public static void main(String[] args) {
        testReference();
//        testReferenceStamped();
//        testReferenceMark();
    }

    /**
     * 带标记原子引用对象
     */
    private static void testReferenceMark() {
        Member member = new Member("小明", 12);
        AtomicMarkableReference<Member> reference = new AtomicMarkableReference<>(member, true);
        reference.compareAndSet(member, new Member("hh", 20), true, false);
        System.out.println(reference.getReference().toString());
    }

    /**
     * 设置带邮戳的原子类对象(版本号)
     */
    private static void testReferenceStamped() {
        Member member = new Member("小明", 12);
        // 创建带版本号的原子类对象
        AtomicStampedReference<Member> reference = new AtomicStampedReference<>(member, 1);
        boolean bFlag = reference.compareAndSet(member, new Member("哈哈", 18), 2, 4);
        System.out.println("版本不对，更新失败:" + bFlag+ ":" + reference.getReference().toString()+":" + reference.getStamp());
        // 比较更新并替换新版本号
        bFlag = reference.compareAndSet(member, new Member("哈哈", 18), 1, 4);
        System.out.println("更新成功:" + bFlag+ ":" + reference.getReference().toString() + ":" + reference.getStamp());
    }

    /**
     * 普通原子操作类
     */
    private static void testReference() {
        Member member = new Member("小明",22);
        AtomicReference<Member> reference = new AtomicReference<>(member); // 建立引用类型关联对象，由此创建原子对象
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
