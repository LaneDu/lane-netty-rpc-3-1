package com.galaxy.provider.handler;

import com.alibaba.fastjson.JSON;
import com.galaxy.provider.anno.RPCService;
import com.galaxy.rpc.common.RpcRequest;
import com.galaxy.rpc.common.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 服务端业务处理类
 * 1.将标有@RPCService注解的bean缓存
 * 2.接收客户端请求
 * 3.根据传递过来的beanName从缓存中查找到对应的bean
 * 4.解析请求中的方法名称. 参数类型 参数信息
 * 5.反射调用bean的方法
 * 6.给客户端进行响应
 * @author lane
 * @date 2021年06月04日 下午7:26
 */
@Component
@ChannelHandler.Sharable //可以共享的handler不然只能调用1次
public class RPCServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {
    //map缓存
    private static final Map SERVICE_INSTANCE_MAP = new ConcurrentHashMap();
    /**
     * 1.将标有@RPCService注解的bean缓存
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取注解的Bean集合
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RPCService.class);
        if (serviceMap!=null&&serviceMap.size()>0){
            //遍历bean
            Set<Map.Entry<String, Object>> entries = serviceMap.entrySet();
            for (Map.Entry<String,Object> entry :entries) {
                Object value = entry.getValue();
                Class<?>[] interfaces = value.getClass().getInterfaces();
                if (interfaces==null||interfaces.length==0){
                    throw new RuntimeException("必须实现对外的接口");
                }
                //以第一个接口名字作为key放入缓存中
                SERVICE_INSTANCE_MAP.put(interfaces[0].getName(),value);

            }
        }

    }
    //通道读取就绪事件
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("服务端接收请求："+msg);
        //1.接收客户端请求- 将msg转化RpcRequest对象
        RpcRequest rpcRequest = JSON.parseObject(msg, RpcRequest.class);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            //业务处理
            rpcResponse.setResult(handler(rpcRequest));
        }catch (Exception e){
            e.printStackTrace();
            rpcResponse.setError(e.getMessage());
        }
        //6.给客户端进行响应
        channelHandlerContext.writeAndFlush(JSON.toJSONString(rpcResponse));
        System.out.println("服务端发送响应："+JSON.toJSONString(rpcResponse));
    }
    /**
     * 业务处理逻辑
     *
     * @return
     */
    public Object handler(RpcRequest rpcRequest) throws InvocationTargetException {
        Object userService = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        if(userService==null){
            throw new RuntimeException("根据beanName找不到对应的类beanName"+rpcRequest.getClassName());
        }
        //4.解析请求中的方法名称. 参数类型 参数信息
        Class<?> userServiceClass = userService.getClass();
        String methodName = rpcRequest.getMethodName();
        Object[] parameters = rpcRequest.getParameters();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        //5.反射调用bean的方法- CGLIB反射调用
        FastClass fastClass = FastClass.create(userServiceClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);
        Object invoke = method.invoke(userService, parameters);

        return invoke;
    }

}
