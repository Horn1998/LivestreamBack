package com.horn.energy_blockchain;

import com.horn.energy_blockchain.Utils.RabbitmqServer;
import com.horn.energy_blockchain.Utils.WebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
//@MapperScan("com.horn.energy_blockchain.Mapper")
public class EnergyBlockchainApplication implements CommandLineRunner {

    @Autowired
    WebSocketServer webSocketServer;
    @Autowired
    RabbitmqServer rabbitmqServer;

    public static void main(String[] args) {
        SpringApplication.run(EnergyBlockchainApplication.class, args);
    }
    //防止netty把springboot阻塞
    @Async
    @Override
    public void run(String... args) throws Exception {
//        rabbitmqServer.run();
        webSocketServer.run();
    }
}
