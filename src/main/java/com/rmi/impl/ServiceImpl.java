package com.rmi.impl;

import com.rmi.IService;

/**
 * @ClassName ServiceImpl
 * @Description TODO
 * @Author wangjian
 * @Date 2021/4/19 下午9:05
 * @Version 1.0
 **/
public class ServiceImpl implements IService {


    @Override
    public String sayHello(String message) {
        System.out.println("say hello 接口");
        return "【Echo】：" + message;
    }
}
