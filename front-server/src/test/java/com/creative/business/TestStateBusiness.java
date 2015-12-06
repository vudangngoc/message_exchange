package com.creative.business;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class TestStateBusiness {

  @Test
  public void testGetStatus(){
    //Given
    StateBusiness service = new StateBusiness();
    service.setDeviceState("123@demo", "ON");
    service.setDeviceState("456@demo", "ON");
    service.setDeviceState("789@demo", "ON");
    //When
    Map<String,String> result = service.getAllDevice();
    //Then
    assertTrue(3 <= result.size());
  }
  
}
