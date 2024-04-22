package com.server;

import rateLimit.RateLimiter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    RateLimiter rateLimiter;
    TcpServer(){}
    TcpServer(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }
    final int serverPort = 8080;
    public void createServer() {
        try (
                // Create a server socket that listens on the specified port
                ServerSocket serverSocket = new ServerSocket(serverPort);
        ) {
            System.out.println("Server is listening on port " + serverPort);

            // Wait for incoming connections
            while (true) {
                // Accept the client connection
                try{
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                    // Create streams for reading from and writing to the client socket
                    new Thread(() -> {
                        try {
                            Handler handler = new Handler(clientSocket);
                            while (true) {
                                if(rateLimiter.isRateLimited()) {
                                    handler.rejectRequest();
                                }else {
                                    rateLimiter.increase();
                                    boolean requestServed = handler.serveRequest();
                                    rateLimiter.decrease();
                                    if(!requestServed) break;
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }catch (IOException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
