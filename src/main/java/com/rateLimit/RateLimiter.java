package com.rateLimit;

public interface RateLimiter {
    boolean isRateLimited();
    Long increase();
    Long decrease();
}
