package com.creative.service;

import org.junit.Test;

import com.creative.GlobalConfig;
import com.creative.disruptor.MortalHandler;

public class TestStateService extends ServiceTest {
  
  public TestStateService(){
    super();
    System.out.println("=======================================================================");
    System.out.println("Init TestStateService");
    try {      
      MortalHandler service = new MortalHandler(StateService.class,
          Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.WORKER_LIFE_TIME)));
      
      disrupt.injectServices(service);
      disrupt.startDisruptor();
      
    } catch (InstantiationException | IllegalAccessException e) {
      
    }
  }
  @Test
  public void testSetState(){
    //Given
    MockContext context = new MockContext();
    context.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_4@demo;DATA:OFF}");
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //event.context.
    //When
    //Then
    System.out.println(context.getResponse());
  }

  @Test
  public void testGetStatus(){
    //Given
    MockContext context = new MockContext();
    context.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_4@demo;DATA:OFF}");
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    context.setRequest("{COMMAND:STATE_STATUS}");
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //event.context.
    //When
    //Then
    System.out.println(context.getResponse());
  }
}
