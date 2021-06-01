package com.galaxy.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * nio服务端
 * @author lane
 * @date 2021年06月01日 下午8:19
 */
public class ServerChannel {

    public static void main(String[] args) throws IOException, InterruptedException {


//        1. 打开一个服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

//        2. 绑定对应的端口号
        serverSocketChannel.bind(new InetSocketAddress(7070));

//        3. 通道默认是阻塞的，需要设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        System.out.println("服务器启动成功。。。");
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
//        4. 检查是否有客户端连接 有客户端连接会返回对应的通道
            if (socketChannel==null){
                System.out.println("没有被阻塞可以执行其它的事情");
                TimeUnit.SECONDS.sleep(2);
                continue;
            }

//        5. 获取客户端传递过来的数据,并把数据放在byteBuffer这个缓冲区中
            ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            //返回值:
            //正数: 表示本次读到的有效字节个数.
            //0 : 表示本次没有读到有效字节.
            //-1 : 表示读到了末尾
            System.out.println("客户端发来的信息是："+new String(byteBuffer.array(),0,read, StandardCharsets.UTF_8));
//        6. 给客户端回写数据
            socketChannel.write(ByteBuffer.wrap("已收到信息，答案是2".getBytes(StandardCharsets.UTF_8)));
//        7. 释放资源
            socketChannel.close();
        }

    }




}
