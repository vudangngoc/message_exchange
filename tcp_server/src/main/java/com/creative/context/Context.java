package com.creative.context;



import java.net.Socket;
import com.lmax.disruptor.EventFactory;

public class Context {
	private Socket client;
	public Socket getClient() {return client;}
	public void setClient(Socket client){
		this.client = client;
	}
	private IData data;	
	public final static EventFactory<Context> EVENT_FACTORY = new EventFactory<Context>() {
		public Context newInstance() {
			return new Context();
		}
	};
	public void setData(IData data) {
		this.data = data;		
	}
	public IData getData(){
		return data;
	}
}
