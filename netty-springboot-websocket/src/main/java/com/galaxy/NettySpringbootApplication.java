package com.galaxy;

import com.galaxy.netty.NettyWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//实现CommandLineRunner重写run方法可以使得在Spring boot启动之后再运行run方法
@SpringBootApplication
public class NettySpringbootApplication implements CommandLineRunner {

    @Autowired
    NettyWebSocketServer nettyWebSocketServer;

    public static void main(String[] args) {
        SpringApplication.run(NettySpringbootApplication.class, args);
    }
    //项目启动后启动netty服务器
    @Override
    public void run(String... args) throws Exception {
        new Thread(nettyWebSocketServer).start();
    }
}
