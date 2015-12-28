package com.creative.disruptor;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.creative.GeneralService;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class MortalHandler implements EventHandler<DisruptorEvent> {
  public MortalHandler(Class<? extends WorkHandler<DisruptorEvent>> clazz, int lifeTime) throws InstantiationException, IllegalAccessException{
  	logger.setLevel(Level.INFO);
    this.clazz = clazz;
    LIFE_TIME = lifeTime;
    RefreshWorker();
  }
  final static Logger logger = Logger.getLogger(MortalHandler.class);
  private int LIFE_TIME = 1024;
  private WorkHandler<DisruptorEvent> handler;
  private Class<? extends WorkHandler<DisruptorEvent>> clazz;
  int lifeTime = LIFE_TIME;
  public void onEvent(DisruptorEvent arg0) throws Exception {

  }

  private void ReduceLifeTime() {
    this.lifeTime -= 1;
    if(lifeTime < 1)
      try {
        RefreshWorker();
      } catch (InstantiationException | IllegalAccessException e ) {
        logger.debug(e);
      }
  }

  private void RefreshWorker() throws InstantiationException, IllegalAccessException {
    handler = clazz.newInstance();
    lifeTime = LIFE_TIME;
    logger.debug("Refresh Worker " + clazz.toString());
  }

  public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
    handler.onEvent(event);
    this.ReduceLifeTime();
  }
  public boolean canHandle(String command){
    if(handler instanceof GeneralService)
      return ((GeneralService)handler).canHandle(command);
    else return false;
  }
}
