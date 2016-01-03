package com.creative;


import com.creative.disruptor.DisruptorEvent;
import com.lmax.disruptor.EventHandler;

public interface GeneralService extends EventHandler<DisruptorEvent>{
  public static final String COMMAND = "COMMAND";
  public static final String FROM = "FROM";
  public static final String TO = "TO";
  public boolean canHandle(String command);
  public String getStatus();
  public String getInfo();
}
