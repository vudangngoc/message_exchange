package com.creative.disruptor;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.service.GeneralService;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorHandler {
	public DisruptorHandler(int ringSize){
		this.exec = Executors.newCachedThreadPool();
		this.disruptor = new Disruptor<Context>(Context.EVENT_FACTORY, ringSize, exec);
		System.out.println("Init Disruptor with " + ringSize + " slot(s)");
	}
	
	@SuppressWarnings("unchecked")
	public void injectServices(EventHandler<Context> service){
		if(service == null) return;
		disruptor.handleEventsWith(service);
	}
	@SuppressWarnings("unchecked")
	public void injectServices(List<EventHandler<Context>> services){		
		if(services.size() == 0) return;
		disruptor.handleEventsWith(services.get(0));
		if(services.size() > 1) 
			for(int i = 1; i < services.size(); i++)
				disruptor.after(services.get(i - 1)).handleEventsWith(services.get(i));		
	}
	public boolean startDisruptor(){
		this.ringBuffer = disruptor.start();
		System.out.println("Disruptor started");
		return false;
	}
	public void stopDisruptor(){
		disruptor.shutdown();
		exec.shutdown();
	}
	public void push(Socket client,IData data){
		long sequence = ringBuffer.next();
		Context event = ringBuffer.get(sequence);
		event.setData(data);
		event.setClient(client);
		ringBuffer.publish(sequence);
	}
	RingBuffer<Context> ringBuffer;
	Disruptor<Context> disruptor;
	EventHandler<Context> eventHandle;
	ExecutorService exec;
}
