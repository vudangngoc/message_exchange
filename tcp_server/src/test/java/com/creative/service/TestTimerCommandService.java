package com.creative.service;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.creative.GlobalConfig;
import com.creative.context.JsonData;
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
    //When
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }    
    //Then
    JsonData data = new JsonData(context.getResponse());
    assertFalse("".equals(data.get(TimerCommandService.TIMER_ID)));
    context.setRequest("{COMMAND:TIMER_REMOVE_ALL}");
    disrupt.push(context);
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
      e.printStackTrace();
    }
    //When
    context.setRequest("{COMMAND:TIMER_LIST}");
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //Then    
    JsonData data = new JsonData(context.getResponse());   
    assertEquals(3,data.getList("data").size());
    context.setRequest("{COMMAND:TIMER_REMOVE_ALL}");
    disrupt.push(context);
  }

  @Test
  public void testRemove(){
    System.out.println("testRemove");
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
      e.printStackTrace();
    }
    JsonData data = new JsonData(context.getResponse());
    String id = data.get(TimerCommandService.TIMER_ID);
    //When
    context.setRequest("{COMMAND:TIMER_REMOVE;" + TimerCommandService.TIMER_ID + ":" + id + "}");
    disrupt.push(context);
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //Then
    data.setData(context.getResponse());
    assertEquals(id,data.get(TimerCommandService.TIMER_ID));
    context.setRequest("{COMMAND:TIMER_REMOVE_ALL}");
    disrupt.push(context);
  }

  @Test
  public void testConvertToString(){    
    TimerCommand tc = new TimerCommand("COMMAND", "2015-NOV-28 21-58-01 +0700", RepeatType.REPEAT_DAILY);
    String timeLong = "1448722681000";
    TimerCommandService service = new TimerCommandService();
    JsonData data = new JsonData(service.convertString(tc));
    assertEquals("COMMAND", data.get(TimerCommandService.COMMAND));
    assertEquals(timeLong, data.get(TimerCommandService.TIME_FIRE));
    assertFalse("".equals(data.get(TimerCommandService.TIMER_ID)));
  }
}
