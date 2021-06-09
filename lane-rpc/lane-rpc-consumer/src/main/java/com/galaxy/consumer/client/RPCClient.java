package com.galaxy.consumer.client;

import com.galaxy.consumer.handler.RPCClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 客户端
 * 1.连接Netty服务端
 * 2.提供给调用者主动关闭资源的方法
 * 3.提供消息发送的方法
 * @author lane
 * @date 2021年06月05日 上午11:06
 */
public class RPCClient {
    private String ip;
    private int port;
    private Channel channel;
    public RPCClient(String ip, int port) throws InterruptedException {
        this.ip = ip;
        this.port = port;
        initClient();
    }

    private EventLoopGroup  group ;
    //新建处理类
    private RPCClientHandler rpcClientHandler = new RPCClientHandler();
    //新建线程池
    ExecutorService threadPool = Executors.newCachedThreadPool();
    public void initClient() throws InterruptedException {
        try {
            //1.创建线程组
            group = new NioEventLoopGroup();
            //2.创建启动助手
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,Boolean.TRUE)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //String的编解码器
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            //添加处理类
                            pipeline.addLast(rpcClientHandler);
                        }
                    });
            //4.连接Netty服务端
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            System.out.println("Netty RPC 客户端启动成功");
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (channel!=null){
                channel.close();
            }
            if (group!=null){
                group.shutdownGracefully();
            }


        }


    }
    /**
     * 提供给调用者主动关闭资源的方法
     */
    public void close(){

        if (channel!=null){
            channel.close();
        }
        if (group!=null){
            group.shutdownGracefully();
        }


    }
    /**
     * 提供消息发送的方法
     */
    public Object sendMsg(String msg) throws ExecutionException, InterruptedException {

        rpcClientHandler.setRequestMsg(msg);
        Future future = threadPool.submit(rpcClientHandler);
        Object object = future.get();

        return object;


    }

}
