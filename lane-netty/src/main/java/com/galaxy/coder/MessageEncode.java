package com.galaxy.coder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author lane
 * @date 2021年06月02日 下午6:45
 */
public class MessageEncode extends MessageToMessageEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,Object msg, List list) throws Exception {
        System.out.println("正在进行消息的编码.....");
        //转化为string
        String message = (String) msg;
        //传递到管道的下一个handler，那么下一个handler获取到de消息就是编码码后的类型的msg
        list.add(Unpooled.copiedBuffer(message,CharsetUtil.UTF_8));

    }
}