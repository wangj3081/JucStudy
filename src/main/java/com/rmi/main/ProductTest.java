package com.rmi.main;

import com.rmi.IService;
import com.rmi.impl.ServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @ClassName ProductTest
 * @Description TODO
 * @Author wangjian
 * @Date 2021/4/19 下午9:06
 * @Version 1.0
 **/
public class ProductTest {

    public static void main(String[] args) throws RemoteException {
        // 实例化
        IService service = new ServiceImpl();
        // 开启本地服务
        IService remoteService = (IService) UnicastRemoteObject.exportObject(service, 666);
        // 创建注册中心
        Registry registry = LocateRegistry.createRegistry(999);
        // 往注册中心注册服务
        registry.rebind("sayHello", remoteService);
        System.out.println("启动啦。。。");
    }
}
