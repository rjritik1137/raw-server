package com.hashing;

import java.util.HashMap;
import java.util.Map;

public class ConsistentHashingTest {
    public static void main(String[] args) {
        ConsistentHashing hashMap = getConsistentHashing();
        Map<Integer, Integer> count = new HashMap<>();
        for(int i=0;i<800;i++) {
           int x = hashMap.getServerIdForKey(i);
           if(count.containsKey(x)) {
               count.put(x, count.get(x) + 1);
           }else {
               count.put(x, 1);
           }
        }
        System.out.println(count);
        hashMap.removeServer(100);
        count.clear();
        for(int i=0;i<802;i++) {
            int x = hashMap.getServerIdForKey(i);
            if(count.containsKey(x)) {
                count.put(x, count.get(x) + 1);
            }else {
                count.put(x, 1);
            }
        }
        System.out.println(count);
    }

    private static ConsistentHashing getConsistentHashing() {
        ConsistentHashing hashMap = new ConsistentHashing(20, (value)-> {
            final int prime = 31;
            final int multiplier = 17;
            int hash = 1;
            while (value != 0) {
                int byteValue = value & 0xFF; // Extract the least significant byte
                hash = prime * hash + (multiplier * byteValue);
                value >>>= 8;
            }
            return hash;
        });
        hashMap.addServer(0, 30);
        hashMap.addServer(100, 30);
        hashMap.addServer(300, 30);
        hashMap.addServer(600, 30);
        return hashMap;
    }
}
