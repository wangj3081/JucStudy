package com.aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author wangjian
 * @version 1.0
 * @see ClientWriteHandler
 * @since JDK1.8
 */
public class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private CountDownLatch latch;

    private AsynchronousSocketChannel socketChannel;

    public ClientWriteHandler(AsynchronousSocketChannel socketChannel, CountDownLatch latch) {
        this.socketChannel = socketChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        // 存在数据内容,写入到 AsynchronousSocketChannel 之中
        if (buffer.hasRemaining()) {
            this.socketChannel.write(buffer, buffer, this);
        } else {
            // 不存在数据，则需要读取数据
            ByteBuffer readByteBuffer = ByteBuffer.allocate(100);
            this.socketChannel.read(readByteBuffer, readByteBuffer, new ClientReadHandler(this.socketChannel, this.latch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.err.println("【ClientWriteHandler】写入数据错误，请重新连接: " + exc.getMessage());
        try {
            this.socketChannel.close();
            // 停止线程
            this.latch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exc.printStackTrace();
    }
}
