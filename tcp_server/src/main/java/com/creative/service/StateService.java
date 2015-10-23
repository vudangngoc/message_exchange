package com.creative.service;

import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorEvent;
import org.json.JSONException;

public class StateService implements GeneralService {
	public StateService(){
	}
	ConcurrentHashMap <String,String> messageList = new ConcurrentHashMap<String, String>();

	public boolean canHandle(String command) {
		if(command == null || "".equals(command)) return false;
		return command.startsWith("STATE_");
	}

	@Override
	public void onEvent(DisruptorEvent event) throws Exception {
		Context context = event.context;
		String command;
		try{
			command = context.getRequest().get(GeneralService.COMMAND);
		}catch(JSONException e){
			return;
		}
		if(!canHandle(command)) return;
		String result = "";
		switch(command){
		case "STATE_GET":
			result = messageList.get(context.getRequest().get(GeneralService.FROM));
			break;
		default:
			messageList.put(context.getRequest().get(GeneralService.TO), context.getRequest().toString());
			result = "OK";
			break;
		}
		context.setResponse(result);

	}
}
