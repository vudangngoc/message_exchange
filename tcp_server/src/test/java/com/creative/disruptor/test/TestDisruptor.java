package com.creative.disruptor.test;

import java.util.ArrayList;

import org.junit.Test;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorHandler;
import com.creative.disruptor.MortalHandler;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class TestDisruptor {
	@Test
	public void test1PublishTo3Consummer(){
		ArrayList<EventHandler<Context>> arr = new ArrayList<EventHandler<Context>>();

		try {
			arr.add(new MortalHandler(TestMortalHandler.class, 100));
		} catch (InstantiationException | IllegalAccessException e) {}
		DisruptorHandler disruptor = new DisruptorHandler(512);
		disruptor.injectServices(arr);
		disruptor.startDisruptor();
		for(long i = 0; i<1000;i++)
		disruptor.push(null,new TestData(i));
	}
	public static class TestMortalHandler implements WorkHandler<Context>{
		private static int lifeTime = 0;
		public TestMortalHandler(){
			lifeTime += 1;
			System.out.println("Init TestMortalHandler round: " + TestMortalHandler.lifeTime);
		}
		@Override
		public void onEvent(Context event) throws Exception {
			
			
		}

}
	public static class TestData implements IData{
		private long data;
		public TestData(long i){
			this.data = i;
		}
		@Override
		public String get(String key) {
			// TODO Auto-generated method stub
			return data + "";
		}}
}
