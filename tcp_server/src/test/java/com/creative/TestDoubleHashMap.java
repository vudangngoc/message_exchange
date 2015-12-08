package com.creative;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDoubleHashMap {

  @Test
  public void testPut(){
    //Given
    DoubleHashMap<String> map = new DoubleHashMap<>();
    //When
    map.put("key@key", "value");
    //Then
    assertEquals(1,map.size());
  }
  
  @Test
  public void testGet(){
    //Given
    DoubleHashMap<String> map = new DoubleHashMap<>();
    //When
    map.put("key@key", "value");
    //Then
    assertEquals("value",map.get("key@key"));
  }
}
