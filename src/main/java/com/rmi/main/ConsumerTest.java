package com.rmi.main;

import com.rmi.IService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * @ClassName ConsumerTest
 * @Description TODO
 * @Author wangjian
 * @Date 2021/4/19 下午9:11
 * @Version 1.0
 **/
public class ConsumerTest {

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry(999);
        System.out.println("远程方法集:" + Arrays.toString(registry.list()));
        IService service = (IService) registry.lookup("sayHello");
        System.err.println(service.sayHello("你好啊"));;
    }
}
