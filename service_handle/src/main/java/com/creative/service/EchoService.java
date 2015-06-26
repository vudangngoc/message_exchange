package com.creative.service;

import java.io.PrintStream;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorHandle;

public class EchoService extends GeneralService {
	private DisruptorHandle disrupt;
	public EchoService(){
		super();
	}
	public void onEvent(Context arg0, long arg1, boolean arg2) throws Exception {
		//arg0.getClient().println(arg0.getMessage().get("COMMAND"));
		arg0.getClient().println("Hello from Echo service");
		arg0.getClient().close();
	}

	@Override
	protected String getMessageHandleList() {
		return "ECHO";
	}
	
	@Override
	protected DisruptorHandle getDisruptorHandle() {
		if(disrupt == null) disrupt = new DisruptorHandle(512);
		return disrupt;
	}

}
