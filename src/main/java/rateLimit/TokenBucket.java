package rateLimit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

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
                       Long count = jedis.decrBy(rateLimitKey, refillRate);
                       // If the new counter value is negative, reset it to zero
                       if (count < 0) {
                           jedis.set(rateLimitKey, "0");
                       }

                   }
               }
           }.start();

       }
    }

    public void increase() {
        Long value = jedis.incr(rateLimitKey);
        if(value > maxBucketSize){
            jedis.set(rateLimitKey, String.valueOf(maxBucketSize));
        }
    }

    public void decrease() {
        Long value = jedis.decr(rateLimitKey);
        if(value < 0) {
            jedis.set(rateLimitKey, "0");
        }
    }

    public boolean isRateLimited(){
        String counter = jedis.get(rateLimitKey);
        if(counter == null) {
            return true;
        }
        return Long.parseLong(counter) >= maxBucketSize;
    }
}
