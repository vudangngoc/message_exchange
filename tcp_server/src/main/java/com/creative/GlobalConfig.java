package com.creative;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalConfig{

  public static final String PORT = "port";
  public static final String WORKER_LIFE_TIME = "worker_life_time";
  public static final String RING_BUFFER_SIZE = "ring_buffer_size";

  private static ConcurrentHashMap<String, String> keys = new ConcurrentHashMap<String, String>();

  public static String setConfig(String key, String value){
    return keys.put(key,value);
  }

  public static String getConfig(String key){
    return keys.get(key);
  }
}