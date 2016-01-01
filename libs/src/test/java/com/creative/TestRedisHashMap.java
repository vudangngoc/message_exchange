package com.creative;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestRedisHashMap {
	private Jedis jedis= new Jedis("s1.thietbithongminh.info",11502);


  @Test
  public void testPut(){
    //Given
    RedisHashMap map = new RedisHashMap(this.jedis,"test");
    map.clear();
    //When
    map.put("key@key", "{COMMAND:STATE_SET;FROM:X;TO:esp7_4@demo;DATA:OFF}");
    map.put("key@key1", "value");
    map.put("key1@key", "value");
    //Then
    assertEquals(3,map.size());
  }
  
  @Test
  public void testGet(){
    //Given
  	RedisHashMap map = new RedisHashMap(this.jedis,"test");
  	map.clear();
    //When
    map.put("key@key", "value");
    //Then
    assertEquals("value",map.get("key@key"));
  }
  
  @Test
  public void testValues(){
    //Given
  	RedisHashMap map = new RedisHashMap(this.jedis,"test");
  	map.clear();
    //When
    map.put("key@key", "value");
    map.put("key@key1", "value");
    map.put("key1@key", "value");
    //Then
    List<String> result = new ArrayList<>( map.values());
    assertEquals(3,result.size());
    assertTrue(result.contains("value"));
  }
  @Test
  public void testContainKey(){
    //Given
    RedisHashMap map = new RedisHashMap(this.jedis,"test");
    map.clear();
    //When
    map.put("key@key", "value");
    map.put("key@key1", "value");
    map.put("key1@key", "value");
    //Then
    assertTrue(map.containsKey("key@key1"));
  }
  
  @Test
  public void testRemove(){
    //Given
    RedisHashMap map = new RedisHashMap(this.jedis,"test");
    map.clear();
    //When
    map.put("key@key", "value");
    map.put("key@key1", "value");
    map.put("key1@key", "value");
    //Then
    assertTrue("value".equals(map.remove("key@key")));
    assertTrue("".equals(map.get("key@key")));
  }
  
  @Test
  public void testReplace(){
    //Given
    RedisHashMap map = new RedisHashMap(this.jedis,"test");
    map.clear();
    //When
    map.put("key@key", "value");
    map.put("key@key1", "value");
    map.put("key1@key", "value");
    //Then
    assertTrue("value".equals(map.replace("key@key","value1")));
    assertTrue("value1".equals(map.get("key@key")));
  }
  
  @Test
  public void testPutPerf(){
    //Given
    RedisHashMap map = new RedisHashMap(this.jedis,"test");
    map.clear();
    //When
    long start = System.currentTimeMillis();
    for(int i = 0; i < 1000; i ++)
    	map.put("key" + i, "value" + i);
    start = System.currentTimeMillis() - start;
    System.out.println("Put 1000 item take " + start);
    //Then
  }
}
