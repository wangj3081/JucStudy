package com.nio.demo.buffer;


import java.nio.ByteBuffer;

public class BufferMain {

    public static void main(String[] args) {
        String strVal = "Hello, Nio buffer";
        // 开辟空间
        ByteBuffer buffer = ByteBuffer.allocate(20);
        System.out.println("开辟空间后的 buffer: position: " + buffer.position() + " 、limit:" + buffer.limit());
        // 写入数据
        buffer.put(strVal.getBytes());
        System.out.println("写入数据后的 buffer: position: " + buffer.position() + " 、limit:" + buffer.limit());
        buffer.flip();
        System.out.println("重置后的 buffer: position: " + buffer.position() + " 、limit:" + buffer.limit());
        byte[] bytes = new byte[buffer.limit()];
        int i = 0;
        while (buffer.hasRemaining()) {
//            System.out.print(buffer.get());
            bytes[i] = buffer.get();
            i++;
        }
        String val = new String(bytes);
        System.out.println(val);
        System.out.println("取完数据后的 buffer: position: " + buffer.position() + " 、limit:" + buffer.limit());
        buffer.clear();
        System.out.println("清空数据后的 buffer: position: " + buffer.position() + " 、limit:" + buffer.limit());

    }
}
