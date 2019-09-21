package com.aio.client;

import com.nio.util.InputUtil;

import java.io.IOException;

/**
 * 客户端发送主类
 * @author wangjian
 * @version 1.0
 * @see ClientMainStart
 * @since JDK1.8
 */
public class ClientMainStart {

    public static void main(String[] args) throws IOException {
        AIOClientThread clientThread = new AIOClientThread();
        new Thread(clientThread).start();
        while (clientThread.sendMessage(InputUtil.getString("输入要发送的内容:"))) {
            ;;
        }
    }
}
