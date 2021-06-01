package com.galaxy.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 有注册中心的服务器
 * @author lane
 * @date 2021年06月01日 下午10:15
 */
public class SelectorServerChannel {
    public static void main(String[] args) throws IOException {
        //1. 打开一个服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2. 绑定对应的端口号
        serverSocketChannel.bind(new InetSocketAddress(7070));
        //3. 通道默认是阻塞的，需要设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //4. 创建选择器
        Selector selector = Selector.open();
        //5. 将服务端通道注册到选择器上,并指定注册监听的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功......");
        while (true) {
            //6. 检查选择器是否有事件
            int select = selector.select(2000);
            System.out.println("检测是否有连接...");
            if (select==0){
                continue;
            }
            //7. 获取事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //8. 判断事件是否是客户端连接事件SelectionKey.isAcceptable()
                if (selectionKey.isAcceptable()){
                    SocketChannel socketChannel =  serverSocketChannel.accept();
                    System.out.println("客户端已经成功连接。。。"+socketChannel);
                    //因为selector要轮询访问，每个通道的事件，设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //9. 得到客户端通道,并将通道注册到选择器上, 并指定监听事件为OP_READ
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                //10. 判断是否是客户端读就绪事件SelectionKey.isReadable()
                if(selectionKey.isReadable()){
                    //11. 得到客户端通道,读取数据到缓冲区
                    SocketChannel socketChannel2 = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = socketChannel2.read(byteBuffer);
                    if(read>0) {
                        System.out.println("读取到的客户端信息是：" + new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
                        //12. 给客户端回写数据
                        socketChannel2.write(ByteBuffer.wrap("已收到信息，答案是2".getBytes(StandardCharsets.UTF_8)));
                        //释放资源
                        socketChannel2.close();
                    }

                }
                //13. 从集合中删除对应的事件, 因为防止二次处理.
                iterator.remove();
            }

        }

    }



}
