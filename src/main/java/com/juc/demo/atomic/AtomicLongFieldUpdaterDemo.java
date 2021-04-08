package com.juc.demo.atomic;

import com.juc.demo.util.EntityUtil;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * @ClassName AtomicLongFieldUpdater
 * @Description 并发情况下，属性安全更新用例
 * @Author wangjian
 * @Date 2021/3/21 下午12:34
 * @Version 1.0
 **/
public class AtomicLongFieldUpdaterDemo {

    public static void main(String[] args) {
        new AtomicLongFieldUpdaterDemo().test();
    }

    private void  test() {
        Book book = new Book(1001L, "admin");
        book.setId(2001L);
        System.out.println(book.toString());
    }

    class Book extends EntityUtil {

        private volatile long id;

        private String title;

        public Book(Long id, String title) {
            this.id = id;
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            // 属性必须是 volatile 修饰的
            AtomicLongFieldUpdater<Book> fieldUpdater = AtomicLongFieldUpdater
                    .newUpdater(Book.class, "id");
            fieldUpdater.compareAndSet(this, this.id, id);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
