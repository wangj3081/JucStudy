package com.rmi;

import java.rmi.Remote;

/**
 * @ClassName IService
 * @Description TODO
 * @Author wangjian
 * @Date 2021/4/19 下午9:03
 * @Version 1.0
 **/
public interface IService extends Remote {

    /**
     * rmi接口，远程接口的异常信息需要完全抛出
     * @param message
     * @Exception Exception
     * @return
     */
    String sayHello(String message) throws Exception;
}
