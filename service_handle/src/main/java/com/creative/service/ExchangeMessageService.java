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
		case "MESSAGE_SEND":
		case "STATE_SET":
			messageList.put(event.getData().get(GeneralService.TO), event.getData().toString());
			result = "OK";
			break;
		case "MESSAGE_GET":
		case "STATE_GET":
			result = messageList.put(event.getData().get(GeneralService.TO),"");			
			break;
		}
		event.getClient().print(result);
		event.getClient().close();
	}
	ConcurrentHashMap <String,String> messageList = new ConcurrentHashMap<String, String>();
	private DisruptorHandle disrupt;

	@Override
	protected String getMessageHandleList() {
		// TODO Auto-generated method stub
		return "MESSAGE_SEND,MESSAGE_GET,STATE_SET,STATE_GET";
	}

	@Override
	protected DisruptorHandle getDisruptorHandle() {
		if(disrupt == null) disrupt = new DisruptorHandle(512);
		return disrupt;
	}

}
