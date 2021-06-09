package com.galaxy.rmi.client;

import com.galaxy.rmi.pojo.User;
import com.galaxy.rmi.service.IUserService;
import com.galaxy.rmi.service.UserServiceImpl;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI客户端
 * @author lane
 * @date 2021年06月04日 下午4:08
 */
public class RMIClient {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        //1.获取Registry实例
        Registry registry = LocateRegistry.getRegistry("127.0.0.1",7070);
        //2.通过Registry实例查找远程对象
        IUserService userService = (IUserService) registry.lookup("userService");

        User user = userService.getUserById(2);

        System.out.println(user);
    }
}
