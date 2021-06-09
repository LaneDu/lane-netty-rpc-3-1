package com.galaxy.provider;

import com.galaxy.provider.anno.RPCService;
import com.galaxy.provider.server.RPCServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lane
 * @date 2021年06月04日 下午6:33
 */
@SpringBootApplication
public class ServerBootstrapApplication implements CommandLineRunner {
    @Autowired
    RPCServer rpcServer;
    public static void main(String[] args) {
        SpringApplication.run(ServerBootstrapApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{

            rpcServer.startServer("127.0.0.1", 7070);

        }).start();
    }
}
