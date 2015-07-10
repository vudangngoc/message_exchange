package com.creative.service;

import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandle;

public class ExchangeMessageService extends GeneralService {

	public void onEvent(Context event, long sequence, boolean endOfBatch)
			throws Exception {
		String command = event.getData().get(GeneralService.COMMAND);
		String result = "";
		switch(command){
		case "MESSAGE_GET":
			result = messageList.get(event.getData().get(GeneralService.TO));			
			break;
		default:
			messageList.put(event.getData().get(GeneralService.TO), event.getData().toString());
			result = "OK";
			break;
		}
		event.getClient().print(result);
		event.getClient().close();
	}
	ConcurrentHashMap <String,String> messageList = new ConcurrentHashMap<String, String>();
	private DisruptorHandle disrupt;

	@Override
	protected boolean canHandle(String command) {
		return true;
	}

	@Override
	protected DisruptorHandle getDisruptorHandle() {
		if(disrupt == null) disrupt = new DisruptorHandle(512);
		return disrupt;
	}
}
