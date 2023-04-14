package com.sense.svcdmobile.client;

import redis.clients.jedis.Jedis;

public class Compare {
  public static void main(String[] args) {
    Jedis JRedis0 = new Jedis("192.168.0.134", 6379);
    JRedis0.auth("abc123");
    JRedis0.select(0);
    Jedis JRedis1 = new Jedis("192.168.0.134", 6379);
    JRedis1.auth("abc123");
    JRedis1.select(1);
    for (String key : JRedis0.keys("*")) {
      String a = JRedis0.get(key);
      String b = JRedis1.get(key);
      if (a != null && a.trim().length() > 0 && !a.equals(b))
        System.out.println(String.valueOf(key) + ">" + a + ">" + b); 
    } 
    JRedis0.close();
    JRedis1.close();
  }
}
