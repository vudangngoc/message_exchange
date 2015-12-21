package com.creative.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;

public class OnePublisherWorkerPool {
  public OnePublisherWorkerPool(WorkHandler<DisruptorEvent> ... w){
    workerPool = new WorkerPool<DisruptorEvent>(DisruptorEvent.EVENT_FACTORY, DisruptorEvent.EXCEPTION_HANDLE,w );
    EXECUTOR = Executors.newFixedThreadPool(w.length);
    ringBuffer = workerPool.start(Executors.newCachedThreadPool());
    ringBuffer.newBarrier();
    
  }
  public void push(Context data){
    long sequence = ringBuffer.next();
    DisruptorEvent event = ringBuffer.get(sequence);
    event.context = data;
    ringBuffer.publish(sequence);
  }
  ExecutorService EXECUTOR;
  RingBuffer<DisruptorEvent> ringBuffer;
  WorkerPool<DisruptorEvent> workerPool;
}