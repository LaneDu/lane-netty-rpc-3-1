package com.galaxy.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 * @author lane
 * @date 2021年06月01日 下午12:55
 */
public class ClientDemo {

    public static void main(String[] args) {

        try {
            //创建socket连接
            Socket socket = new Socket("127.0.0.1",7070);
            OutputStream outputStream = socket.getOutputStream();
            String message = "请问1+1 = ?";
            outputStream.write(message.getBytes());
            System.out.println("客户端发送信息："+message);
            //获取客户端返回的信息
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            //字节流转为字符串
            String msg = new String(bytes,0,read).trim();
            System.out.println("服务器返回的信息是："+msg);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
