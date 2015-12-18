package com.creative.service;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import com.creative.GlobalConfig;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
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
		IData data = DataObjectFactory.createDataObject(context.getResponse());
		assertFalse("".equals(data.get(TimerCommandService.TIMER_ID)));
		context.setRequest("{COMMAND:TIMER_REMOVE_ALL}");
		disrupt.push(context);
	}

	@Test
	public void testTimerList(){
		System.out.println("testTimerList");
		//Given
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		MockContext context = new MockContext();
		context.setRequest(TimerCommandService.createAddTimeCommand(
		    "from", "to", RepeatType.REPEAT_MINUTELY.name(), 
		    df.format(new Date()), "ON"));
		disrupt.push(context);
		disrupt.push(context);
		disrupt.push(context);
		try {
			Thread.sleep(1000000);
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
		IData data = DataObjectFactory.createDataObject(context.getResponse());
		assertTrue(3 <= data.getList("data").size());
//		context.setRequest("{COMMAND:TIMER_REMOVE_ALL}");
//		disrupt.push(context);
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
		IData data = DataObjectFactory.createDataObject(context.getResponse());
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
		context.setRequest(TimerCommandService.createRemoveTimeCommand(id));
		disrupt.push(context);
	}

	@Test
	public void testModify(){
		System.out.println("testModify");
		//Given
		MockContext context = new MockContext();
		context.setRequest(TimerCommandService.createAddTimeCommand("from", "to", TimerCommandService.REPEAT_HOURLY, "2015-Nov-28 21-00-01 +0700", "OFF"));
		disrupt.push(context);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(context.getResponse());
		String id = DataObjectFactory.createDataObject(context.getResponse()).get(TimerCommandService.TIMER_ID);
		//WHen
		String message = TimerCommandService.createEditTimeCommand(id, TimerCommandService.REPEAT_WEEKLY, "2015-Nov-28 21-58-01 +0700", "ON");
		MockContext context1 = new MockContext();
		context1.setRequest(message);
		context1.setResponse("");
		disrupt.push(context1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(context1.getResponse());
		//Then
		assertTrue(!"{}".equals(context1.getResponse()));
		IData result = DataObjectFactory.createDataObject(context1.getResponse());
		assertEquals(id,result.get(TimerCommandService.TIMER_ID));
		assertEquals("REPEAT_WEEKLY",result.get(TimerCommandService.REPEATLY));
		IData command = DataObjectFactory.createDataObject(result.get(TimerCommandService.COMMAND));
		assertEquals("ON",command.get("DATA"));
//		context.setRequest("{COMMAND:TIMER_REMOVE_ALL}");
//		disrupt.push(context);
	}

	@Test
	public void testConvertToString(){    
		TimerCommand tc = new TimerCommand("COMMAND", "2015-Nov-28 21-58-01 +0700", RepeatType.REPEAT_DAILY);
		String timeLong = "1448722681000";
		TimerCommandService service = new TimerCommandService();
		IData data = DataObjectFactory.createDataObject(service.convertString(tc));
		assertEquals("COMMAND", data.get(TimerCommandService.COMMAND));
		assertEquals("2015-Nov-28 21-58-01 +0700", data.get(TimerCommandService.TIME_FIRE));
		assertFalse("".equals(data.get(TimerCommandService.TIMER_ID)));
	}

	@Test
	public void testCreateAdd(){
		//Given
		String message = TimerCommandService.createAddTimeCommand("from", "to", "REPEAT_WEEKLY", "2015-Nov-28 21-58-01 +0700", "ON");
		//WHen
		IData result = DataObjectFactory.createDataObject(message);
		//THen
		assertEquals("from",result.get(TimerCommandService.FROM));
		assertEquals("to",result.get(TimerCommandService.TO));
		assertEquals("REPEAT_WEEKLY",result.get(TimerCommandService.REPEATLY));
		assertEquals("2015-Nov-28 21-58-01 +0700",result.get(TimerCommandService.TIME_FIRE));
		assertEquals("ON",result.get(TimerCommandService.STATE));
		assertEquals("TIMER_SET",result.get(TimerCommandService.COMMAND));  	
	}
	@Test
	public void testCreateEdit(){
		//Given
		String message = TimerCommandService.createEditTimeCommand("id", "REPEAT_WEEKLY", "2015-Nov-28 21-58-01 +0700", "ON");
		//WHen
		IData result = DataObjectFactory.createDataObject(message);
		//THen
		assertEquals("id",result.get(TimerCommandService.TIMER_ID));  	
		assertEquals("REPEAT_WEEKLY",result.get(TimerCommandService.REPEATLY));
		assertEquals("2015-Nov-28 21-58-01 +0700",result.get(TimerCommandService.TIME_FIRE));
		assertEquals("ON",result.get(TimerCommandService.STATE));
		assertEquals("TIMER_EDIT",result.get(TimerCommandService.COMMAND));  	
	}
	@Test
	public void testDelete(){
		//Given
		String message = TimerCommandService.createRemoveTimeCommand("id");
		//WHen
		IData result = DataObjectFactory.createDataObject(message);
		//THen
		assertEquals("TIMER_REMOVE",result.get(TimerCommandService.COMMAND));
		assertEquals("id",result.get(TimerCommandService.TIMER_ID));
	}
}
