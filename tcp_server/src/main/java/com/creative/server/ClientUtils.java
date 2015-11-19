package com.creative.server;

import java.util.concurrent.ConcurrentHashMap;

public class ClientUtils {
  private static ConcurrentHashMap<String, String> keys = new ConcurrentHashMap<String, String>();

  public static String addKey(String domain, String keyEncrypt){
    return keys.put(domain, keyEncrypt);
  }

  public static String getKey(String domain){
    return keys.get(domain);
  }
}
