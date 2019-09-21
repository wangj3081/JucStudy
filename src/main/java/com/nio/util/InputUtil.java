package com.nio.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文本输入工具类
 * @author wangjian
 * @version 1.0
 * @see InputUtil
 * @since JDK1.8
 */
public class InputUtil {

    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));

    public static String getString(String prompt) {
        String result = null;

        while (result == null || "".equals(result)) {
            try {
                System.out.println(prompt);
                result = KEYBOARD_INPUT.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
