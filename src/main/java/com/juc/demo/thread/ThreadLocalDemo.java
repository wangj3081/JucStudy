package com.juc.demo.thread;

import lombok.Data;

/**
 * @ClassName ThreadLocalDemo
 * @Description ThreadLocal 解决多线程下资源引用传递的线程安全问题
 *              ThreadLocal 本身通过保存当前操作线程本身的方式(相当于给每个操作线程添加的标记)，来保证了多线程下资源操作同步的问题
 * @Author wangjian
 * @Date 2020/3/21 9:46 下午
 * @Version 1.0
 **/
public class ThreadLocalDemo {

    public static void main(String[] args) {
        Channel channel = new Channel();
        new Thread(()->{
            Message message = new Message();
            message.setInfo(Thread.currentThread().getName()+":发送的第一条消息");
            channel.setMessage(message);
            System.out.println(channel.getMessage().getInfo());
        }, "线程A").start();
        new Thread(()->{
            Message message = new Message();
            message.setInfo(Thread.currentThread().getName()+":发送的第二条消息");
            channel.setMessage(message);
            System.out.println(channel.getMessage().getInfo());
        }, "线程B").start();
        new Thread(()->{
            Message message = new Message();
            message.setInfo(Thread.currentThread().getName()+":发送的第三条消息");
            channel.setMessage(message);
            System.out.println(channel.getMessage().getInfo());
        }, "线程C").start();
    }

}

class Channel {
    private  Message message;
    private final ThreadLocal<Message> THREAD_LOCAL = new ThreadLocal<>();

    public  void setMessage(Message m) {
//        this.message = m;
        THREAD_LOCAL.set(m);
    }

    public Message getMessage() {
//        return message;
        return THREAD_LOCAL.get();
    }
}

@Data
class Message {
    private String info;
}

