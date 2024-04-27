package rateLimit;

import redis.clients.jedis.Jedis;

public class RedisRateLimiter {
    private final Jedis jedis;
    private final String key;
    private final int maxTokens;
    private final double fillRate; // tokens per second
    private final Object lock = new Object();

    private long lastRefillTime = System.currentTimeMillis();

    public RedisRateLimiter(Jedis jedis, String key, int maxTokens, double fillRate) {
        this.jedis = jedis;
        this.key = key;
        this.maxTokens = maxTokens;
        this.fillRate = fillRate;
    }

    public boolean acquire() {
        synchronized (lock) {
            long now = System.currentTimeMillis();
            String tokens = jedis.get(key);
            long currentTokens = tokens == null ? 0 : Long.parseLong(tokens);

            // Calculate the number of tokens that should be in the bucket by now
            long expectedTokens = Math.min(maxTokens, currentTokens + (long) ((now - lastRefillTime) / 1000 * fillRate));

            // Refill the bucket if needed
            if (expectedTokens > currentTokens) {
                jedis.set(key, String.valueOf(expectedTokens));
                lastRefillTime = now;
            }

            // Consume a token if available
            if (expectedTokens >= 1) {
                jedis.set(key, String.valueOf(expectedTokens - 1));
                return true;
            }

            return false; // No tokens available
        }
    }
}