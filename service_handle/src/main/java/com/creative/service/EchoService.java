package com.creative.service;

import com.creative.context.Context;

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

}
