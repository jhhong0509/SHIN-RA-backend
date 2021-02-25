package com.example.shinra.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class SocketConfig {

    @Value("${server.socket.port}")
    private Integer port;

    private SocketIOServer socketIOServer;

    @Bean
    public SocketIOServer webSocketServer() {
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setPort(port);

        SocketIOServer server = new SocketIOServer(configuration);
        server.start();
        this.socketIOServer = server;

        return server;
    }

    @PreDestroy
    public void socketStop() {
        socketIOServer.stop();
    }
}
