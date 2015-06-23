package com.creative.service;

import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.context.IData;

public class ExchangeMessageService extends GeneralService {

	public void onEvent(Context event, long sequence, boolean endOfBatch)
			throws Exception {
		String command = event.getData().get(GeneralService.COMMAND);
		String result = "";
		switch(command){
		case "SEND":
			messageList.put(event.getData().get(GeneralService.TO), event.getData().get(GeneralService.DATA));
			result = "OK";
			break;
		case "GET":
			result = messageList.put(event.getData().get(GeneralService.TO),"");			
			break;
		}
		event.getClient().print(result);
		event.getClient().close();
	}
	ConcurrentHashMap <String,String> messageList = new ConcurrentHashMap<String, String>();
	@Override
	public boolean processMessage(PrintStream client, IData data) {
		try{
			if(!"".equals(data.get(GeneralService.TO))){
				this.disrupt.push(client, data);
				return true;
			}
		}
		catch(Exception e){
			
		}
		return false;
	}

	@Override
	protected String getMessageHandleList() {
		// TODO Auto-generated method stub
		return null;
	}

}
