package com.nio.demo.buffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;

/**
 * 管道
 */
public class ChannelMain {

    public static void main(String[] args) {
//        fileChannelTest();
        pipeChannelTest();

    }

    /**
     * 通过多线程实现的管道消息读取，可以用于线程之间的通信处理
     */
    private static void pipeChannelTest() {

        try {
            Pipe open = Pipe.open();
            // 读取消息线程
            new Thread(()-> {
                Pipe.SourceChannel source = open.source();
                // 开辟读取数据缓冲区
                ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                try {
                    if (source.isOpen()) {
                        // 向缓冲区写入数据
                        int count = source.read(byteBuffer);
                        byteBuffer.flip();
                        System.out.println(Thread.currentThread().getName() + " 读取: " + new String(byteBuffer.array(), 0, count));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "读取线程").start();
            // 发送消息线程
            new Thread(()->{
                // 发送消息通道
                Pipe.SinkChannel sink = open.sink();
                // 开辟缓冲区
                ByteBuffer byteBuffer = ByteBuffer.allocate(20);
                // 向缓冲区写入数据
                byteBuffer.put("Hello, Pipe Channel".getBytes());
                // 重置缓冲区
                byteBuffer.flip();
                if (sink.isOpen()) {
                    try {
                        // 在通道中写入缓冲区数据
                        sink.write(byteBuffer);
                        System.out.println(Thread.currentThread().getName() + "写入数据：" + new String(byteBuffer.array()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, "发送线程").start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用 FileChannel 读取文件
     */
    private static void fileChannelTest() {
        File file = new File("E:" + File.separator + "es部署.txt");
        try {
            // 此处只能使用 FileInputStream, 因为只有 FileInputStream 才有 getChannel 的方法
            FileInputStream inputStream = new FileInputStream(file);
            FileChannel channel = inputStream.getChannel();
            // 开辟一个缓冲区，获取文件内容
            ByteBuffer byteBuffer = ByteBuffer.allocate(20);
            // 存储写入的内容
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int count = 0;
            while ((count = channel.read(byteBuffer)) != -1) {
                // 重置缓冲区 position
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    // 写入内存流之中
                    byteArrayOutputStream.write(byteBuffer.get());
                }
                // 清空缓冲区再读取
                byteBuffer.clear();
            }
            // 由于文件格式为 GBK，所以读取格式设置为 GBK 防止乱码
            String str = new String(byteArrayOutputStream.toByteArray(), "GBK");
            System.out.println(str);
            channel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
