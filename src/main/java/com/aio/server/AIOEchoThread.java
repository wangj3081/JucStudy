package com.aio.server;

import com.nio.info.ServiceInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author wangjian
 * @version 1.0
 * @see AIOEchoThread
 * @since JDK1.8
 */
public class AIOEchoThread implements Runnable {

    private CountDownLatch latch;
    // 异步服务器端 Socket
    private AsynchronousServerSocketChannel serverSocketChannel;

    /**
     * 建立服务器端处理线程
     */
    public AIOEchoThread() throws IOException {
        // 等待的计数器
        this.latch = new CountDownLatch(1);
        // 开启服务器处理
        this.serverSocketChannel = AsynchronousServerSocketChannel.open();
        // 绑定端口
        this.serverSocketChannel.bind(new InetSocketAddress(ServiceInfo.SERVER_PORT));
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    @Override
    public void run() {
        this.serverSocketChannel.accept(this, new AcceptHandler());
        try{
            // 进入阻塞状态
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
