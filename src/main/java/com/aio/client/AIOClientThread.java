package com.aio.client;

import com.nio.info.ServiceInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 客户端连接类
 * @author wangjian
 * @version 1.0
 * @see AIOClientThread
 * @since JDK1.8
 */
public class AIOClientThread implements Runnable {

    private CountDownLatch latch;

    private AsynchronousSocketChannel socketChannel;

    public AIOClientThread() throws IOException {
        this.latch = new CountDownLatch(1);
        // 开启一个异步的 socketChannel
        this.socketChannel = AsynchronousSocketChannel.open();
        // 连接服务端
        this.socketChannel.connect(new InetSocketAddress(ServiceInfo.SERVER_HOST, ServiceInfo.SERVER_PORT));
    }

    @Override
    public void run() {
        try {
            // 进行等待消息处理结束
            latch.await();
            // 关闭客户端连接
            this.socketChannel.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于发送消息的方法
     * @param message
     * @return
     */
    public boolean sendMessage(String message) {
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        // 向缓冲区写入数据
        byteBuffer.put(message.getBytes());
        // 重置缓冲区
        byteBuffer.flip();
        this.socketChannel.write(byteBuffer, byteBuffer, new ClientWriteHandler(this.socketChannel, this.latch));
        if ("exit".equalsIgnoreCase(message)) {
            try {
                TimeUnit.SECONDS.sleep(1);
                // 退出客户端连接
                this.latch.countDown();
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
