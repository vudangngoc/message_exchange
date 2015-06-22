package com.creative.disruptor;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.service.GeneralService;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorHandle {
	public DisruptorHandle(){
		this.exec = Executors.newCachedThreadPool();
		// Preallocate RingBuffer with 1024 ValueEvents
		this.disruptor = new Disruptor<Context>(Context.EVENT_FACTORY, 1024, exec);

	}
	@SuppressWarnings("unchecked")
	public void injectService(GeneralService service){		
		final EventHandler<Context> hanler = service;
		disruptor.handleEventsWith(hanler);
		this.ringBuffer = disruptor.start();
	}
	public void stopDisruptor(){
		disruptor.shutdown();
		exec.shutdown();
	}
	public void push(PrintStream client,IData data){
		long sequence = ringBuffer.next();
		Context event = ringBuffer.get(sequence);
		event.setClient(client);
		event.setData(data);
		ringBuffer.publish(sequence);
	}
	RingBuffer<Context> ringBuffer;
	Disruptor<Context> disruptor;
	ExecutorService exec;
}
