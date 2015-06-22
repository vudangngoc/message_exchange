package com.creative.service;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorHandle;
import com.lmax.disruptor.EventHandler;

public abstract class GeneralService implements EventHandler<Context>{
	public GeneralService(){
		disrupt.injectService(this);
	}
	protected DisruptorHandle disrupt = new DisruptorHandle();
	public void processMessage(PrintStream client,IData data){
		if (this.getMessageHandleList().contains(data.get("COMMAND"))){
			this.disrupt.push(client,data);
		}
	}
	protected abstract String getMessageHandleList();
}
