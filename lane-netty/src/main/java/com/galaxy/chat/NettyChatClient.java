package com.galaxy.chat;

import com.galaxy.coder.MessageCodec;
import com.galaxy.coder.NettyClientHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * Netty chat 客户端
 * @author lane
 * @date 2021年06月02日 下午4:16
 */
public class NettyChatClient {
    //服务端IP和端口号
    private String ip;
    private int port;

    public NettyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    public void run() {

        //1. 创建线程组
        NioEventLoopGroup group = null;
        try {
            //1. 创建线程组
             group = new NioEventLoopGroup();
            //2. 创建客户端启动助手
            Bootstrap  bootstrap = new Bootstrap();
            //3. 设置线程组
            bootstrap.group(group)
                    //4. 设置客户端通道实现为NIO
                    .channel(NioSocketChannel.class)
                    //5. 创建一个通道初始化对象
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        //6. 向pipeline中添加自定义业务处理handler
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加解码handler
                            socketChannel.pipeline().addLast("decode",new StringDecoder());
                            //添加编码器
                            socketChannel.pipeline().addLast("encode",new StringEncoder());
                            //6. 向pipeline中添加自定义业务处理handler
                            socketChannel.pipeline().addLast(new NettyChatClientHandler());
                        }
                    });

            //7. 启动客户端,等待连接服务端,同时将异步改为同步
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("chat客户端"+channel.localAddress().toString().substring(1)+"启动成功");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                //向服务端发送消息
                channel.writeAndFlush(msg);
            }
            //8. 关闭通道(监听关闭状态)
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyChatClient nettyChatClient = new NettyChatClient("127.0.0.1", 7070);
        nettyChatClient.run();
    }

}
