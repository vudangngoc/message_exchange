package com.creative.service;

import org.junit.Test;

import com.creative.GlobalConfig;
import com.creative.disruptor.MortalHandler;

public class TestTimerCommandService extends ServiceTest {

  public TestTimerCommandService(){
    super();
    System.out.println("=======================================================================");
    System.out.println("Init TestTimerCommandService");
    try {      
      MortalHandler timerService = new MortalHandler(TimerCommandService.class,
          Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.WORKER_LIFE_TIME)));

      disrupt.injectServices(timerService);
      disrupt.startDisruptor();

    } catch (InstantiationException | IllegalAccessException e) {

    }
  }
  @Test
  public void testAddTimerCommand(){
    System.out.println("testAddTimerCommand");
    //Given
    MockContext context = new MockContext();
    context.setRequest("{COMMAND:TIMER_SET;FROM:esp7@demo;TO:esp7@demo;STATE:OFF;"
        + "TIME_FIRE:2015-NOV-18 21-58-01 +0700;REPEATLY:REPEAT_HOURLY}");
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
  public void testTimerList(){
    System.out.println("testTimerList");
    //Given
    MockContext context = new MockContext();
    context.setRequest("{COMMAND:TIMER_SET;FROM:esp7@demo;TO:esp7@demo;STATE:OFF;"
        + "TIME_FIRE:2015-NOV-28 21-58-01 +0700;REPEATLY:REPEAT_HOURLY}");
    disrupt.push(context);
    disrupt.push(context);
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(context.getResponse());
    //When
    context.setRequest("{COMMAND:TIMER_LIST}");
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    //Then
    System.out.println(context.getResponse());
  }
}
