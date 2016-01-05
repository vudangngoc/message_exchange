package com.creative.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.creative.GlobalConfig;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.disruptor.MortalHandler;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestStateService extends ServiceTest {

	public TestStateService(){
		super();
		System.out.println("=======================================================================");
		System.out.println("Init TestStateService");

		StateService service = new StateService();
		service.redisPool  = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 11502);
		disrupt.injectServices(service);
		disrupt.startDisruptor();


	}
	@Test
	public void testSetState(){
		//Given
		MockContext context = new MockContext();
		context.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_4@demo;DATA:OFF}");
		//When
		disrupt.push(context);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Then
		assertNotNull(context.getResponse());
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
		context2.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_3@demo1;DATA:OFF}");
		disrupt.push(context2);
		MockContext context3 = new MockContext();
		context3.setRequest("{COMMAND:STATE_SET;FROM:X;TO:esp7_2@demo2;DATA:OFF}");
		disrupt.push(context3);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		//When
		context3.setRequest("{COMMAND:STATE_STATUS}");
		disrupt.push(context3);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		//Then
		assertNotNull(context3.getResponse());
		IData data = DataObjectFactory.createDataObject(context3.getResponse());
		assertTrue(3 <= data.getList("data").size());
	}
}
