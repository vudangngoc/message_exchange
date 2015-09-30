package com.creative.service;

import java.io.DataOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandler;


public class StateService extends GeneralService {

	public void onEvent(Context event, long sequence, boolean endOfBatch)
			throws Exception {
		String command = event.getData().get(GeneralService.COMMAND);
		String result = "";
		switch(command){
		case "STATE_GET":
			result = messageList.get(event.getData().get(GeneralService.FROM));			
			break;
		default:
			messageList.put(event.getData().get(GeneralService.TO), event.getData().toString());
			result = "OK";
			break;
		}
		
		DataOutputStream outStream = new DataOutputStream(event.getClient().getOutputStream());
		if(outStream != null){
			outStream.writeBytes(result == null ? "" : result);
			outStream.flush();
			//event.getClient().close();
		}
	}
	ConcurrentHashMap <String,String> messageList = new ConcurrentHashMap<String, String>();
	private DisruptorHandler disrupt;

	@Override
	protected boolean canHandle(String command) {
		return command.startsWith("STATE_");
	}

	@Override
	protected DisruptorHandler getDisruptorHandler() {
		if(disrupt == null) disrupt = new DisruptorHandler(512);
		return disrupt;
	}
}
