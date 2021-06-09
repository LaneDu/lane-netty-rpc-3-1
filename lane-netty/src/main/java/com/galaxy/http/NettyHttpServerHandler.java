package com.galaxy.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * http处理
 * @author lane
 * @date 2021年06月03日 下午2:21
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //1.判断请求是不是HTTP请求
        if(httpObject instanceof HttpRequest){

            DefaultHttpRequest httpRequest = (DefaultHttpRequest) httpObject;
            System.out.println("请求路径："+httpRequest.uri());
            if ("/favicon.ico".equals(httpRequest.uri())) {
                System.out.println("图标不响应");
                return;
            }
            //2.给浏览器进行响应response
            ByteBuf byteBuf = Unpooled.copiedBuffer("你好，我是Netty服务端", CharsetUtil.UTF_8);
            //建立响应
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,byteBuf);
            //设置下响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());
            channelHandlerContext.writeAndFlush(response);

        }


    }
}
