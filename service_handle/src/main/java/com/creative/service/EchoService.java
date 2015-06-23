package com.creative.service;

import java.io.PrintStream;

import com.creative.context.Context;
import com.creative.context.IData;

public class EchoService extends GeneralService {
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
	public boolean processMessage(PrintStream client, IData data) {
		if ("ECHO".equals(data.get("COMMAND"))){
			this.disrupt.push(client,data);
			return true;
		}
		return false;
	}

}
