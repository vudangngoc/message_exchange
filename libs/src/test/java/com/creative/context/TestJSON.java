package com.creative.context;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestJSON {
  @Test
  public void testParse(){
    //Given
    JsonData data = new JsonData();
    //CheckOK
    assertTrue(data.setData("{a:b;c:d}"));
    assertEquals("b", data.get("a"));
    assertEquals("d", data.get("c"));
    assertEquals("", data.get("d"));
    //CHeck parse faile
    assertFalse(data.setData(""));
    assertFalse(data.setData("{"));
    assertFalse(data.setData("}"));
    assertFalse(data.setData("{;;}"));
    assertFalse(data.setData("{;{}}"));
  }
  
  @Test
  public void testRecursiveParse(){
    //Given
    JsonData data = new JsonData();
    //Then
    assertTrue(data.setData("{a:b;k:{c:d}}"));
    assertEquals("{\"c\":\"d\"}", data.get("k"));
  }
}
