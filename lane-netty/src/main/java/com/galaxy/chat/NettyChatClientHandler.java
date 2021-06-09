package com.galaxy.chat;

import io.netty.channel.*;

/**
 * Netty 聊天室客户端处理器
 * @author lane
 * @date 2021年06月02日 下午4:23
 */
public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }
}
