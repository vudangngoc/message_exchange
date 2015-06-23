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
	public static final String COMMAND = "COMMAND";
	public static final String DATA = "DATA";
	public static final String FROM = "FROM";
	public static final String TO = "TO";
	protected DisruptorHandle disrupt = new DisruptorHandle();
	public abstract boolean processMessage(PrintStream client,IData data);
	protected abstract String getMessageHandleList();
}
