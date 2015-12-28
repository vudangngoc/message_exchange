package com.creative.disruptor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.creative.context.Context;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DisruptorHandler {
  public DisruptorHandler(int ringSize){
    this.exec = Executors.newCachedThreadPool();
    this.disruptor = new Disruptor<DisruptorEvent>(DisruptorEvent.EVENT_FACTORY, ringSize, exec);
    logger.info("Init Disruptor with " + ringSize + " slot(s)");
    logger.setLevel(Level.INFO);
  }
  final static Logger logger = Logger.getLogger(DisruptorHandler.class);
  @SuppressWarnings("unchecked")
  public void injectServices(EventHandler<DisruptorEvent> service){
    if(service == null) return;
    disruptor.handleEventsWith(service);
  }
  @SuppressWarnings("unchecked")
  public void injectServices(List<EventHandler<DisruptorEvent>> services){
    if(services.size() == 0) return;
    disruptor.handleEventsWith(services.get(0));
    if(services.size() > 1)
      for(int i = 1; i < services.size(); i++)
        disruptor.after(services.get(i - 1)).handleEventsWith(services.get(i));
  }
  public boolean startDisruptor(){
    this.ringBuffer = disruptor.start();
    logger.info("Disruptor started");
    return false;
  }
  public void stopDisruptor(){
    disruptor.shutdown();
    exec.shutdown();
    if(logger.isInfoEnabled()){
      logger.info("Disruptor stopped");
    }
  }
  public void push(Context data){
    long sequence = ringBuffer.next();
    DisruptorEvent event = ringBuffer.get(sequence);
    event.context = data;
    ringBuffer.publish(sequence);
  }
  RingBuffer<DisruptorEvent> ringBuffer;
  Disruptor<DisruptorEvent> disruptor;
  EventHandler<DisruptorEvent> eventHandle;
  ExecutorService exec;
}
