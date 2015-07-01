package com.creative.service;

import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandle;

public class StateService extends GeneralService {
	ConcurrentHashMap <String,ConcurrentHashMap<String,String>> nodeList = new ConcurrentHashMap<String, ConcurrentHashMap<String,String>>();
	ConcurrentHashMap <String,String> nodeType = new ConcurrentHashMap<String, String>();
	private DisruptorHandle disrupt;
	public StateService(){
		nodeType.put("","");
	}
	@Override
	public void onEvent(Context event, long arg1, boolean arg2) throws Exception {
		String command = event.getData().get(GeneralService.COMMAND);
		String result = "";
		switch(command){
		case "STATE_SET":
			//messageList.put(event.getData().get(GeneralService.TO), event.getData().get(GeneralService.DATA));
			result = "OK";
			break;
		case "STATE_GET":
			//result = messageList.put(event.getData().get(GeneralService.TO),"");			
			break;
		}
		event.getClient().print(result);
		event.getClient().close();

	}

	@Override
	protected DisruptorHandle getDisruptorHandle() {
		if(disrupt == null) disrupt = new DisruptorHandle(512);
		return disrupt;
	}

	@Override
	protected String getMessageHandleList() {
		return "STATE_SET,STATE_GET";
	}

}
