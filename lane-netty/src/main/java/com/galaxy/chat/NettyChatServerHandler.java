package com.galaxy.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天室处理handler
 * @author lane
 * @date 2021年06月02日 下午3:51
 */
public class NettyChatServerHandler extends SimpleChannelInboundHandler {

    public static List<Channel> channelList = new ArrayList<>();

    /**
     * 通道就绪事件
     * @author lane
     * @date 2021/6/3 上午11:38
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //获取channel
        Channel channel = ctx.channel();
        System.out.println("[server]:"+channel.remoteAddress().toString().substring(1)+"上线了");
        //当有新的客户端连接的时候, 将通道放入集合
        channelList.add(channel);
    }
    /**
     * 通道未就绪事件
     * @author lane
     * @date 2021/6/3 上午11:41
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelList.remove(channel);
        System.out.println("[server]:"+channel.remoteAddress().toString().substring(1)+"下线了");
        //当有客户端断开连接的时候,就移除对应的通道

    }
    /**
     * 通道读取事件
     * @author lane
     * @date 2021/6/3 上午11:43
     * @param channelHandlerContext
     * @param o
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //当前发送消息的通道, 当前发送的客户端连接
        Channel channel = channelHandlerContext.channel();
        String msg = (String) o;
        //发送消息给每一个客户端
        for (Channel channel1: channelList) {
            //排除自身通道
            if (channel1!=channel){
                channel1.writeAndFlush("["+channel.remoteAddress().toString().substring(1)+"]说:"+msg);
            }
        }
    }
    /**
     * 异常捕获
     * @author lane
     * @date 2021/6/3 上午11:51
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        channelList.remove(channel);
        System.out.println("[server]:"+channel.remoteAddress().toString().substring(1)+"异常");
    }
}
