package com.nio.server;

import com.nio.info.ServiceInfo;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIO 服务类
 * @author wangjian
 * @version 1.0
 * @see NIOEchoServer
 * @since JDK1.8
 */
public class NIOEchoServer {

    /**
     * 服务启动方法
     * @throws Exception
     */
    public void start() throws Exception{
        // 1、由于要考虑性能的问题，所以只能创建一个定长的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 2、如果要实现服务端的开发，那么一定需要有一个服务端的 Channel 管理； 开启服务端通道处理
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置成非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 在当前主机中的 9999 端口绑定服务
        serverSocketChannel.bind(new InetSocketAddress(ServiceInfo.SERVER_PORT));
        // 3、开启一个选择器，所有的 Channel 都注册到此选择器之中，利用此选择器的循环来判断是否有新的连接
        Selector selector = Selector.open();
        // 通道注册，连接处理
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 4、所有的用户都要连注册到复路器之中，那么就需要利用循环模式来进行用户状态的判断
        // 保存用户的判断状态
        int keySelect = 0;
        // 持续等待，一直到有用户连接上
        while ((keySelect = selector.select()) > 0) {
            // 获取当前所有连接的信息
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // 获取当前连接的处理状态
                SelectionKey selectionKey = iterator.next();
                // 当前为一个连接请求状态
                if (selectionKey.isAcceptable()) {
                    // 创建一个客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    if (socketChannel != null) {
                        // 启动一个处理线程
                        executorService.submit(new NIOEchoThread(socketChannel));
                    }
                }
                // 移除掉已经处理过的请求
                iterator.remove();
            }
        }
        executorService.shutdown();
        serverSocketChannel.close();
    }

    public static void main(String[] args) {
        try {
            System.err.println("********************服务端已启动，等待连接********************");
            new NIOEchoServer().start();
        } catch (Exception e) {
            System.err.println("服务端启动异常");
            e.printStackTrace();
        }
    }
}
