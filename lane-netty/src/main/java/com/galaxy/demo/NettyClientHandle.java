package com.galaxy.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * Netty 客户端处理器
 * @author lane
 * @date 2021年06月02日 下午4:23
 */
public class NettyClientHandle implements ChannelInboundHandler {

    /**
     * 通道就绪事件
     * @author lane
     * @date 2021/6/2 下午4:25
     * @param channelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        //发送信息
        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("我是Netty客户端", CharsetUtil.UTF_8));
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    System.out.println("数据发送成功");
                }else {
                    System.out.println("数据发送失败");
                }
            }
        });
    }
    //读取信息
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务端信息是:"+byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }



    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }



    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
