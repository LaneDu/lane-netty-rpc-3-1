package com.galaxy.provider.server;

import com.galaxy.provider.handler.RPCServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lane
 * @date 2021年06月04日 下午6:32
 */
@Service
public class RPCServer implements DisposableBean {

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    @Autowired
    RPCServerHandler rpcServerHandler;

    public void startServer(String ip, int port) {
        try {
            //1. 创建线程组
            bossGroup = new NioEventLoopGroup(1);
            workGroup = new NioEventLoopGroup();
            //2. 创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //String的编解码器
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            //业务处理类
                            pipeline.addLast(rpcServerHandler);

                        }
                    });
            //4.绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(ip, port).sync();
            System.out.println("==========服务端启动成功==========");

            //监听关闭同步
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workGroup != null) {
                workGroup.shutdownGracefully();
            }
        }

    }


    @Override
    public void destroy() throws Exception {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }


}
