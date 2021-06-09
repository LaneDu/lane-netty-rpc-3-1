package com.galaxy.coder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author lane
 * @date 2021年06月02日 下午3:51
 */
public class NettyServerHandle implements ChannelInboundHandler {

    /**
     * 通道的读取事件
     * @author lane
     * @date 2021/6/2 下午3:52
     * @param channelHandlerContext
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("客户端信息是："+ msg);

    }
    /**
     * 读取完成事件
     * @author lane
     * @date 2021/6/2 下午3:55
     * @param channelHandlerContext
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        //Unpooled Netty 提供的一个专门用来操作缓冲区的 工具类
        channelHandlerContext.writeAndFlush("我是Netty服务端");
    }
    /**
     * 异常处理
     * @author lane
     * @date 2021/6/2 下午4:00
     * @param channelHandlerContext
     * @param throwable
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        throwable.printStackTrace();
        channelHandlerContext.close();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {

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


}
