package com.galaxy.rmi.server;

import com.galaxy.rmi.service.IUserService;
import com.galaxy.rmi.service.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI服务端
 * @author lane
 * @date 2021年06月04日 下午4:05
 */
public class RMIServer {

    public static void main(String[] args) throws RemoteException {

        //1.注册Registry实例. 绑定端口
        Registry registry = LocateRegistry.createRegistry(7070);
        //2.创建远程对象
        IUserService userService = new UserServiceImpl();
        //3.将远程对象注册到RMI服务器上即(服务端注册表上)
        registry.rebind("userService", userService);
        System.out.println("---RMI服务端启动成功----");

    }




}
