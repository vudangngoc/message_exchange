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

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestTimerCommandService extends ServiceTest {

	public TestTimerCommandService(){
		super();
		System.out.println("=======================================================================");
		System.out.println("Init TestTimerCommandService");

		TimerCommandService timerService = new TimerCommandService();
		timerService.redisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 11502);
		disrupt.injectServices(timerService);
		disrupt.startDisruptor();
		timerService.startWDT();

	}
	@Test
	public void testAddTimerCommand(){
		System.out.println("testAddTimerCommand"); 
		//Given
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		MockContext context = new MockContext();
		context.setRequest(TimerCommandService.createAddTimeCommand(
				"from", "to", RepeatType.REPEAT_MINUTELY.name(), 
				df.format(new Date()), "ON"));
		//When
		disrupt.push(context);
		while(context.getResponse() == null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}

		//Then
		assertFalse("{}".equals(context.getResponse()));
		IData data = DataObjectFactory.createDataObject(context.getResponse());
		assertFalse("".equals(data.get(TimerCommandService.TIMER_ID)));
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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		//When
		context.setRequest(TimerCommandService.createListTimeCommand());
		disrupt.push(context);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//Then    
		assertTrue(!"{}".equals(context.getResponse()));
		IData data = DataObjectFactory.createDataObject(context.getResponse());
		assertTrue(3 <= data.getList("data").size());
	}

	@Test
	public void testRemove(){
		System.out.println("testRemove");
		//Given
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		MockContext context = new MockContext();
		context.setRequest(TimerCommandService.createAddTimeCommand(
				"from", "to", RepeatType.REPEAT_MINUTELY.name(), 
				df.format(new Date()), "ON"));
		disrupt.push(context);
		while(context.getResponse() == null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
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
		assertTrue(!"{}".equals(context.getResponse()));
		data.setData(context.getResponse());
		assertEquals(id,data.get(TimerCommandService.TIMER_ID));
	}

	@Test
	public void testModify(){
		System.out.println("testModify");
		//Given
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		MockContext context = new MockContext();
		context.setRequest(TimerCommandService.createAddTimeCommand(
				"from", "to", RepeatType.REPEAT_MINUTELY.name(), 
				df.format(new Date()), "ON"));
		disrupt.push(context);
		while(context.getResponse() == null){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		String id = DataObjectFactory.createDataObject(context.getResponse()).get(TimerCommandService.TIMER_ID);
		//WHen
		String message = TimerCommandService.createEditTimeCommand(id, TimerCommandService.REPEAT_WEEKLY, df.format(new Date()), "ON");
		MockContext context1 = new MockContext();
		context1.setRequest(message);
		context1.setResponse("");
		disrupt.push(context1);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		//Then
		System.out.println(context1.getResponse());
		assertTrue(!"{}".equals(context1.getResponse()));
		IData result = DataObjectFactory.createDataObject(context1.getResponse());
		assertEquals("REPEAT_WEEKLY",result.get(TimerCommandService.REPEATLY));
		IData command = DataObjectFactory.createDataObject(result.get(TimerCommandService.COMMAND));
		assertEquals("ON",command.get("DATA"));
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
