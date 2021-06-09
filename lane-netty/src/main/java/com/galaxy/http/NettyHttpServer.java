package com.galaxy.http;

import com.galaxy.chat.NettyChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty chat server
 * @author lane
 * @date 2021年06月03日 上午11:32
 */
public class NettyHttpServer {

    private int port;

    public NettyHttpServer(int port) {
        this.port = port;
    }

    public void run(){

        //1. 创建bossGroup线程组: 处理网络事件--连接事件 默认2 x cpu线程
        NioEventLoopGroup boosGroup = null;
        //2. 创建workerGroup线程组: 处理网络事件--读写事件 默认2 x cpu线程
        NioEventLoopGroup workGroup = null;
        try {
            //1. 创建bossGroup线程组: 处理网络事件--连接事件 默认2 x cpu线程
             boosGroup = new NioEventLoopGroup(1);
            //2. 创建workerGroup线程组: 处理网络事件--读写事件 默认2 x cpu线程
             workGroup = new NioEventLoopGroup();
            //3. 创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //4. 设置bossGroup线程组和workerGroup线程组
            serverBootstrap.group(boosGroup, workGroup)
                    //5. 设置服务端通道实现为NIO
                    .channel(NioServerSocketChannel.class)
                    //6. 参数设置初始化服务器可连接队列大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //7. 创建一个通道初始化对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加编解码器
                            socketChannel.pipeline().addLast("httpCodec",new HttpServerCodec());
                            //8. 向pipeline中添加自定义业务处理handler
                            socketChannel.pipeline().addLast(new NettyHttpServerHandler());
                        }
                    });

            //9. 启动服务端并绑定端口,同时将异步改为同步
//          ChannelFuture future = serverBootstrap.bind(7070).sync();
            ChannelFuture future = serverBootstrap.bind(port);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("端口绑定成功");
                    } else {
                        System.out.println("端口绑定失败");
                    }
                }
            });
            System.out.println("Netty http服务端启动成功");
            //10. 关闭通道(同步监听通道关闭状态)和关闭连接池
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        NettyHttpServer nettyHttpServer = new NettyHttpServer(8080);
        nettyHttpServer.run();
    }


}
