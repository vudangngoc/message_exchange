package com.creative.dns;

import com.creative.context.IData;
import com.creative.disruptor.DisruptorEvent;
import com.lmax.disruptor.WorkHandler;

public class DNSHandler implements WorkHandler<DisruptorEvent>{

  @Override
  public void onEvent(DisruptorEvent event) throws Exception {
  	IData request = event.context.getRequest();
  	String result = "";
  	if(request.get("COMMAND").equals("DNS_GET")){
  		while(DomainServer.isUpdating) Thread.sleep(5); //Waiting for lock
  		result = DomainServer.data.get(request.get("DATA")).getHost();
  	}
    event.context.setResponse(result);
  }

}
