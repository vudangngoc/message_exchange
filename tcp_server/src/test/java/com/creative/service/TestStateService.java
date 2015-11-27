package com.creative.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.creative.GlobalConfig;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
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
    //When
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //Then
    IData data = DataObjectFactory.createDataObject(context.getResponse());
    assertEquals("OK",data.get("STATE"));
  }

  @Test
  public void testGetStatus(){
    //Given
    MockContext context1 = new MockContext();
    context1.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_4@demo;DATA:OFF}");
    disrupt.push(context1);
    MockContext context2 = new MockContext();
    context2.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_3@demo;DATA:OFF}");
    disrupt.push(context2);
    MockContext context3 = new MockContext();
    context3.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_2@demo;DATA:OFF}");
    disrupt.push(context3);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    //When
    context3.setRequest("{COMMAND:STATE_STATUS}");
    disrupt.push(context3);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
    }
    //Then
    IData data = DataObjectFactory.createDataObject(context3.getResponse());
    assertEquals(3,data.getList("data").size());
  }
}
