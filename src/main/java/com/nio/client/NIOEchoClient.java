package com.nio.client;

import com.nio.info.ServiceInfo;
import com.nio.util.InputUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 * @author wangjian
 * @version 1.0
 * @see NIOEchoClient
 * @since JDK1.8
 */
public class NIOEchoClient {

    private ByteBuffer byteBuffer;

    public void start() throws Exception{
        // 开启一个 Socket 客户端通道
        SocketChannel socketChannel = SocketChannel.open();
        // 通用服务器的主机名称和IP地址来进行连接操作
        socketChannel.connect(new InetSocketAddress(ServiceInfo.SERVER_HOST, ServiceInfo.SERVER_PORT));
        // 开辟一个缓冲通道，用于发送消息
        byteBuffer = ByteBuffer.allocate(100);
        boolean bFlag = true;
        while (bFlag) {
            // 清空缓冲区，因为该循环会持续进行
            byteBuffer.clear();
            String str = InputUtil.getString("请输入要发送的信息：");
            // 将数据写入缓冲区之中
            byteBuffer.put(str.getBytes());
            // 重置缓冲区
            byteBuffer.flip();
            // 写入 Socket 客户端通道
            socketChannel.write(byteBuffer);
            // 清空，存储服务端返回的消息
            byteBuffer.clear();
            // 向缓冲区之中读取数据
            int readCount = socketChannel.read(byteBuffer);
            System.err.println(new String(byteBuffer.array(), 0, readCount));
            if ("exit".equalsIgnoreCase(str)) {
                // 输入了退出指令，退出连接状态
                bFlag = false;
            }
        }
        // 关闭客户端连接通道
        socketChannel.close();
    }

    public static void main(String[] args) {
        try {
            new NIOEchoClient().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
