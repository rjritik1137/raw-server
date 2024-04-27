package com.rateLimit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TokenBucket implements RateLimiter{
    int maxBucketSize;

    long refillRate;
    Jedis jedis;
    String rateLimitKey;
    public TokenBucket(int maxBucketSize, long refillRate, String rateLimitKey){
        this.maxBucketSize = maxBucketSize;
        this.refillRate = refillRate;
        this.rateLimitKey = rateLimitKey;
       try(JedisPool pool = new JedisPool("localhost", 8888)){
           try (Jedis jedis = pool.getResource()) {
               this.jedis = jedis;
               jedis.set(rateLimitKey, "0");
           }
           new Thread(){
               @Override
               public void run() {
                   super.run();
                   while(true){
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           break;
                       }
                       decrease();
                   }
               }
           }.start();

       }
    }

    public synchronized Long increase() {
        return jedis.incr(rateLimitKey);
    }

    public synchronized Long decrease() {
        return jedis.decr(rateLimitKey);
    }

    public boolean isRateLimited(){
        return increase() >= maxBucketSize;
    }
}
