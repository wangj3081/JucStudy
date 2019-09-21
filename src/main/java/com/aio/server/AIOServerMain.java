package com.aio.server;

import java.io.IOException;

/**
 *
 * @author wangjian
 * @version 1.0
 * @see AIOServerMain
 * @since JDK1.8
 */
public class AIOServerMain {

  public static void main(String[] args) throws IOException {
    new Thread(new AIOEchoThread()).start();
  }
}
