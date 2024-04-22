package rateLimit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class TokenBucket implements RateLimiter{
    int maxBucketSize = 100;

    long refillRate = 1;
    Jedis jedis = null;
    String rateLimitKey = "ritik";
    public TokenBucket(){
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
        jedis.incr(rateLimitKey);
    }

    public boolean isRateLimited(){
        String counter = jedis.get(rateLimitKey);
        if(counter == null) {
            return true;
        }
        return Long.parseLong(counter) >= maxBucketSize;
    }
}
