package com.creative.context;



import java.io.PrintStream;
import java.net.InetAddress;
import java.util.HashMap;

import com.lmax.disruptor.EventFactory;

public class Context {
	private PrintStream client;
	public PrintStream getClient() {return client;}
	public void setClient(PrintStream client){this.client = client;}
	private IData data;	
	public final static EventFactory<Context> EVENT_FACTORY = new EventFactory<Context>() {
        public Context newInstance() {
            return new Context();
        }
    };
	public void setData(IData data) {
		this.data = data;		
	}
	public IData getData(){return data;}
}
