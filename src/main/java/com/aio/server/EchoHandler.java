package com.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 进行 Echo 回调处理的类定义，读取完成的数据返回的类型是长度
 * @author wangjian
 * @version 1.0
 * @see EchoHandler
 * @since JDK1.8
 */
public class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;
    // 设置一个退出的标记， bExit = true 结束所有操作
    private boolean bExit = false;

    public EchoHandler(AsynchronousSocketChannel socketChannel) {
        // 保存客户端的通道，进行消息的接收与发送
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        // AIO 处理的是将数据先进行接收，接收完成后开启线程
        byteBuffer.flip();
        // 此时客户所有发送来的内容实际上已经被接收完毕了
        // 读取发送过来的数据
        String readMessage = new String(byteBuffer.array(), 0, byteBuffer.remaining()).trim();
        System.out.println("【服务端】接收:" + readMessage);
        String resultMessage = "【ECHO】:" + readMessage;
        if ("exit".equalsIgnoreCase(readMessage)) {
            resultMessage = "【EXIT】，服务退出";
            this.bExit = true;
        }
        // 写入回应数据
        this.echoWrite(resultMessage);
    }

    /**
     * 写入回应的数据
     * @param result
     */
    public void echoWrite(String result) {
        // 开辟缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        // 写入数据
        byteBuffer.put(result.getBytes());
        // 重置缓冲区
        byteBuffer.flip();
        this.socketChannel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                // 如果有数据存在，那么可以进行写入
                if (byteBuffer.hasRemaining()) {
                    EchoHandler.this.socketChannel.write(byteBuffer, byteBuffer, this);
                } else {
                    // 没有数据存在,当前还会继续执行
                    if (EchoHandler.this.bExit == false) {
                        ByteBuffer buffer = ByteBuffer.allocate(100);
                        EchoHandler.this.socketChannel.read(buffer, buffer, new EchoHandler(EchoHandler.this.socketChannel));
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                EchoHandler.this.closeClientChannel();
            }
        });
    }

    private void  closeClientChannel() {
        try {
            System.err.println("【EchoHandler】应答操作失败，关闭客户端连接。");
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 失败直接关闭连接
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        this.closeClientChannel();
    }
}
