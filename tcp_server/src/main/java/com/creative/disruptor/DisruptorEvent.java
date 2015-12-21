package com.creative.disruptor;

import org.apache.log4j.Logger;

import com.creative.context.Context;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;

public class DisruptorEvent {
  final static Logger logger = Logger.getLogger(DisruptorEvent.class);
  public final static EventFactory<DisruptorEvent> EVENT_FACTORY = new EventFactory<DisruptorEvent>() {
    public DisruptorEvent newInstance() {
      return new DisruptorEvent();
    }
  };
  public static ExceptionHandler<DisruptorEvent> EXCEPTION_HANDLE = new ExceptionHandler<DisruptorEvent>() {

    public void handleEventException(Throwable ex, long sequence, DisruptorEvent event) {
      logger.debug(ex.getMessage());      
    }

    public void handleOnStartException(Throwable ex) {
      logger.debug(ex.getMessage());      
    }

    public void handleOnShutdownException(Throwable ex) {
      logger.debug(ex.getMessage());      
    }
  };
  public Context context;
}
