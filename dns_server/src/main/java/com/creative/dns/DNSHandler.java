package com.creative.dns;

import com.creative.disruptor.DisruptorEvent;
import com.lmax.disruptor.WorkHandler;

public class DNSHandler implements WorkHandler<DisruptorEvent>{

  @Override
  public void onEvent(DisruptorEvent event) throws Exception {
    // TODO Auto-generated method stub
  }

}
