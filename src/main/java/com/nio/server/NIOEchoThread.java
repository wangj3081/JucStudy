
package com.nio.server;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 创建一个可以进行用户Echo处理的线程类
 * @author wangjian
 * @version 1.0
 * @see NIOEchoThread
 * @since JDK1.8
 */
public class NIOEchoThread implements Runnable {

    private SocketChannel socketChannel;
    private boolean bFlag = true;

    public NIOEchoThread(SocketChannel socketChannel) throws Exception {
        this.socketChannel = socketChannel;
        System.err.println("【客户端连接成功】连接的客户端地址为：" + this.socketChannel.getRemoteAddress());
    }

    @Override
    public void run() {
        // 开辟一个 100 byte 大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(100);
        try {
            // 持续循环处理
            while (this.bFlag) {
                // 清空缓冲区
                buffer.clear();
                // 写入消息到缓冲区
                int readCount = this.socketChannel.read(buffer);
                // 读取缓冲区的数据
                String readMessage = new String(buffer.array(), 0, readCount);
                System.err.println("【服务端收到的消息】: " + readMessage);
                // 返回的消息
                String writeReturnMessage = "【Echo】:" + readMessage;
                if ("exit".equalsIgnoreCase(readMessage)) {
                    writeReturnMessage = "【Exit】系统已退出";
                    this.bFlag = false;
                }
                // 由于之前已经写入过数据入内，所以需要清空
                buffer.clear();
                // 写入要返回给接收端的数据
                buffer.put(writeReturnMessage.getBytes());
                // 重置缓冲区
                buffer.flip();
                this.socketChannel.write(buffer);
            }
        } catch (Exception e) {
            System.err.println("发生了未知异常：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
