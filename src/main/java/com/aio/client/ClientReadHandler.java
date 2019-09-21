package com.aio.client;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * AIO 读取数据
 * @author wangjian
 * @version 1.0
 * @see ClientReadHandler
 * @since JDK1.8
 */
public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private CountDownLatch latch;

    private AsynchronousSocketChannel socketChannel;

    public ClientReadHandler(AsynchronousSocketChannel socketChannel, CountDownLatch latch) {
        this.socketChannel = socketChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        // 缓冲区重置，现在准备读取数据
        byteBuffer.flip();
        String message = new String(byteBuffer.array(), 0, byteBuffer.remaining()).trim();
        System.err.println("【客户端】读取到的数据: " + message);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        System.err.println("【ClientReadHandler】数据读取错误，请重新连接服务器。。。" + exc.getMessage());
        try {
            this.socketChannel.close();
            // 关闭线程
            this.latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
