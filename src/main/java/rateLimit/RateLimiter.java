package rateLimit;

public interface RateLimiter {
    boolean isRateLimited();
    void increase();
    void decrease();
}
