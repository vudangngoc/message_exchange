package com.creative.service;

import com.creative.GlobalConfig;
import com.creative.disruptor.DisruptorHandler;
import com.creative.server.ClientHandler;

public class ServiceTest {
  protected DisruptorHandler disrupt;
 
  public ServiceTest(){
    System.out.println("Init service test");
    GlobalConfig.setConfig(GlobalConfig.RING_BUFFER_SIZE, "512");
    GlobalConfig.setConfig(GlobalConfig.WORKER_LIFE_TIME, "512");
    disrupt = new DisruptorHandler(Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.RING_BUFFER_SIZE)));
    ClientHandler.disrupt = disrupt;
  }

}
