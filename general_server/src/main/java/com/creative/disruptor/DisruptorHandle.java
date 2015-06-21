package com.creative.disruptor;

import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorHandle {
	@SuppressWarnings("unchecked")
	public DisruptorHandle(){
		this.exec = Executors.newCachedThreadPool();
		// Preallocate RingBuffer with 1024 ValueEvents
		this.disruptor = new Disruptor<Context>(Context.EVENT_FACTORY, 1024, exec);
		final EventHandler<Context> loginHandler = new EventHandler<Context>() {
			// event will eventually be recycled by the Disruptor after it wraps
			public void onEvent(final Context event, final long sequence, final boolean endOfBatch) throws Exception {
				PrintStream body = event.getResponse().getPrintStream();
				body.println("hello from Login handler");
				body.println("You came from " + event.getRequest().getClientAddress().getHostString() + ":" + event.getRequest().getClientAddress().getPort());
				body.close();
			}
		};
		// Build dependency graph
		disruptor.handleEventsWith(loginHandler);
		this.ringBuffer = disruptor.start();
	}
	public void stopDisruptor(){
		disruptor.shutdown();
		exec.shutdown();
	}
	public void push(Request rq, Response rp){
		long sequence = ringBuffer.next();
		Context event = ringBuffer.get(sequence); // Get the entry in the Disruptor
												  // for the sequence
		event.setRequest(rq);
		event.setResponse(rp);
		ringBuffer.publish(sequence);
	}
	RingBuffer<Context> ringBuffer;
	Disruptor<Context> disruptor;
	ExecutorService exec;
}
