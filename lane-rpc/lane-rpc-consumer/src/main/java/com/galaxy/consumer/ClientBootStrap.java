package com.galaxy.consumer;

import com.galaxy.consumer.proxy.RPCClientProxy;
import com.galaxy.rpc.api.IUserService;
import com.galaxy.rpc.pojo.User;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;

/**
 * 客户端启动类
 * @author lane
 * @date 2021年06月05日 下午12:03
 */
public class ClientBootStrap {
    public static void main(String[] args) {
        //此处不能分开写，不然就会调用toString方法出错，卡了我好久
        //request:RpcRequest(requestId=3a4948ce-8b2b-4da2-a79d-5c46273e582a, className=java.lang.Object, methodName=toString, parameterTypes=[], parameters=null)
        IUserService userService = (IUserService)RPCClientProxy.createProxy(IUserService.class);
        User user = userService.getById(2);
        System.out.println("user:"+user);


    }
}
