package com.galaxy.coder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty 客户端
 * @author lane
 * @date 2021年06月02日 下午4:16
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        //1. 创建线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2. 创建客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        //3. 设置线程组
        bootstrap.group(group)
                //4. 设置客户端通道实现为NIO
                .channel(NioSocketChannel.class)
                //5. 创建一个通道初始化对象
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    //6. 向pipeline中添加自定义业务处理handler
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //添加编码器
//                        socketChannel.pipeline().addLast("encoder",new MessageEncode());
                        //添加解码handler
//                        socketChannel.pipeline().addLast("decoder",new MessageDecode());
                        //添加编解码器
                        socketChannel.pipeline().addLast("codec",new MessageCodec());
                        //6. 向pipeline中添加自定义业务处理handler
                        socketChannel.pipeline().addLast(new NettyClientHandle());
                    }
                });

        //7. 启动客户端,等待连接服务端,同时将异步改为同步
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7070).sync();
        System.out.println("客户端启动成功");
        //8. 关闭通道(监听关闭状态)和关闭连接池
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();




    }



}
