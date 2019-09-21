package com.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 回调函数定义
 * 如果此类的回调连接成功，那么一定要返回有一个 AsynchronousSocketChannel 的对象，进行服务器端与客户端的通讯
 * @author wangjian
 * @version 1.0
 * @see AcceptHandler
 * @since JDK1.8
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AIOEchoThread> {


    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AIOEchoThread aioEchoThread) {
        // 创建一个连接操作
        aioEchoThread.getServerSocketChannel().accept(aioEchoThread, this);
        // 分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(100);
        // 创建另外一个回调处理
        socketChannel.read(buffer, buffer, new EchoHandler(socketChannel));
        try {
            System.err.println("【服务端】，客户端：" + socketChannel.getRemoteAddress() + "加入");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AIOEchoThread attachment) {
        System.err.println("【AcceptHandler】服务端程序出现错误:" + exc.getMessage());
        exc.printStackTrace();
    }
}
