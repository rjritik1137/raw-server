package com.server;

import com.rateLimit.RateLimiter;
import com.rateLimit.TokenBucket;

public class Server {
    public static void main(String[] args) {
        RateLimiter rateLimiter = new TokenBucket(5, 1, "RATE-LIMITING");

        TcpServer server = new TcpServer(rateLimiter);
        server.createServer();
    }
}
