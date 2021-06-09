package com.galaxy.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.xml.crypto.KeySelector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 因为异步的，所以用callable来知道获取消息成功
 * @author lane
 * @date 2021年06月05日 上午11:24
 */
public class RPCClientHandler extends SimpleChannelInboundHandler<String> implements Callable {
    //发送的消息
     String requestMsg;
     //接收的消息
     String responseMsg;
     ChannelHandlerContext     channelHandlerContext;


    public void setRequestMsg(String requestMsg) {

        this.requestMsg = requestMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
    /**
     * 通道就绪事件
     * @author lane
     * @date 2021/6/5 上午11:29
     * @param ctx
     */
    @Override
    public  void channelActive(ChannelHandlerContext ctx) throws Exception {
         channelHandlerContext = ctx;
    }
    /**
     * 线程实现消息发送
     * @author lane
     * @date 2021/6/5 上午11:32
     * @return java.lang.Object
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("发送消息："+requestMsg);
        channelHandlerContext.writeAndFlush(requestMsg);
        wait();
        System.out.println("接收消息："+responseMsg);
        return responseMsg;
    }
    /**
     * 通道读取事件
     * @author lane
     * @date 2021/6/5 上午11:32
     * @param channelHandlerContext
     * @param msg
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

        responseMsg = msg;

        notify();
    }





}
