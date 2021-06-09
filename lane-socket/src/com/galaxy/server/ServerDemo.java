package com.galaxy.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务器端
 * @author lane
 * @date 2021年06月01日 下午12:29
 */
public class ServerDemo {

    public static void main(String[] args) throws IOException {
        //创建线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {

            //创建serverSocket监听端口
            ServerSocket serverSocket = new ServerSocket(7070);
            System.out.println("服务器已经启动");
            //等待客户端连接
            while(true){
                //接收到请求
                Socket socket = serverSocket.accept();
                System.out.println("客户端已经建立连接");
                //开辟线程执行
                threadPool.execute(()->{
                    //处理请求
                    try {
                        handle(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭线程池
            threadPool.shutdown();

        }
    }
    public static void handle(Socket socket) throws IOException {

        System.out.println("线程ID:" + Thread.currentThread().getId()
                + "   线程名称:" + Thread.currentThread().getName());

        try {
            //获取socket接收到的信息流
            InputStream inputStream = socket.getInputStream();
            //建立存放数据的对象
            byte[] bytes = new byte[1024];
            //读取信息放入字节数组
            int read = inputStream.read(bytes);
            //字节流转为字符串
            String msg = new String(bytes,0,read);
            System.out.println("客户端信息是："+msg);
            //获取socket的输出流
            OutputStream outputStream = socket.getOutputStream();
            //写入信息，传给客户端
            outputStream.write("我已经接收到你的信息了答案是：2".getBytes());
            System.out.println("已回复客户端信息");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭socket连接
            socket.close();
        }

    }



}
