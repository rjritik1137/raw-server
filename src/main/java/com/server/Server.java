package com.server;

import rateLimit.RateLimiter;
import rateLimit.TokenBucket;

public class Server {
    public static void main(String[] args) {
        RateLimiter rateLimiter = new TokenBucket(100, 1, "RATE-LIMITING");
        TcpServer server = new TcpServer(rateLimiter);
        server.createServer();
    }
}
