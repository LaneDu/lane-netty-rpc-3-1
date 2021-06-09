package com.galaxy.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.galaxy.consumer.client.RPCClient;
import com.galaxy.rpc.common.RpcRequest;
import com.galaxy.rpc.common.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 客户端代理类-创建代理对象
 * 1.封装request请求对象
 * 2.创建RpcClient对象
 * 3.发送消息
 * 4.返回结果
 * @author lane
 * @date 2021年06月05日 上午11:37
 */
public class RPCClientProxy {

     public static Object createProxy(Class serviceClass){

        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass}
                ,new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //1.封装request请求对象
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setMethodName(method.getName());
                rpcRequest.setRequestId(UUID.randomUUID().toString());
                rpcRequest.setClassName(method.getDeclaringClass().getName());

//                request.setClassName(method.getDeclaringClass().getName());
                rpcRequest.setParameters(args);
                rpcRequest.setParameterTypes(method.getParameterTypes());
                //2.创建RpcClient对象
                RPCClient rpcClient = new RPCClient("127.0.0.1",7070);
                try {
                    System.out.println("request:"+rpcRequest);
                    Object response = rpcClient.sendMsg(JSON.toJSONString(rpcRequest));
                    RpcResponse rpcResponse = JSON.parseObject(response.toString(), RpcResponse.class);
                    if (rpcResponse.getError()!=null){
                        throw new RuntimeException(rpcResponse.getError());

                    }
                    //4.返回结果
                    Object result = rpcResponse.getResult();
                    return JSON.parseObject(result.toString(), method.getReturnType());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    rpcClient.close();
                }
            }
        });


     }




}
