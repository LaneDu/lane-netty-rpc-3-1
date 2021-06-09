package com.galaxy.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * nio 客户端
 * @author lane
 * @date 2021年06月01日 下午8:57
 */
public class ClientChannelCopy {
    public static void main(String[] args) throws IOException {

//        1. 打开通道
        SocketChannel socketChannel = SocketChannel.open();

//        2. 设置连接IP和端口号
        socketChannel.connect(new InetSocketAddress("127.0.0.1",7070));
//        3. 写出数据
        socketChannel.write(ByteBuffer.wrap("请问1+1=？".getBytes(StandardCharsets.UTF_8)));
//        4. 读取服务器写回的数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(byteBuffer);
        String s = new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8);
        System.out.println("服务器端返回信息是："+s);

        socketChannel.close();
    }
}
