package com.server;

import com.client.TcpClient;
import rateLimit.EmptyRateLimit;
import rateLimit.RateLimiter;
import rateLimit.TokenBucket;

public class Server {
    public static void main(String[] args) {
        RateLimiter rateLimiter = new TokenBucket(5, 1, "RATE-LIMITING");

        TcpServer server = new TcpServer(rateLimiter);
        server.createServer();
    }
}
