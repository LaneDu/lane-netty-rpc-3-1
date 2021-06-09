package com.galaxy.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 解码器 handle
 * @author lane
 * @date 2021年06月02日 下午6:13
 */
public class MessageDecode extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object msg, List list) throws Exception {
        System.out.println("开始解码中。。。。");
        ByteBuf byteBuf = (ByteBuf) msg;
        //传递到管道的下一个handler，那么下一个handler获取到de消息就是解码后的string 类型的msg
        list.add(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
