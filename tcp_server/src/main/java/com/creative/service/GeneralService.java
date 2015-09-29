package com.creative.service;

import java.net.Socket;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorHandler;
import com.lmax.disruptor.EventHandler;

public abstract class GeneralService implements EventHandler<Context>{
	public GeneralService(){
		getDisruptorHandler().injectServices(this);
		getDisruptorHandler().startDisruptor();
	}
	public static final String COMMAND = "COMMAND";
	public static final String DATA = "DATA";
	public static final String FROM = "FROM";
	public static final String TO = "TO";
	protected abstract DisruptorHandler getDisruptorHandler();
	public boolean processMessage(Socket client,IData data) {
		try{
			if(canHandle(data.get(GeneralService.COMMAND))){
				getDisruptorHandler().push(client, data);
				return true;
			}
		}
		catch(Exception e){}
		return false;
	}
	protected abstract boolean canHandle(String command);
}
