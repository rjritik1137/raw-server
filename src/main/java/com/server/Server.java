package com.server;

public class Server {
    public static void main(String[] args) {
        TcpServer server = new TcpServer();
        server.createServer();
    }
}
