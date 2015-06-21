package com.creative.disruptor;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.lmax.disruptor.EventFactory;

public class Context {
	private Request request;
	public void setRequest(Request rq){this.request = rq;}
	public Request getRequest(){return request;}
	private Response response;
	public void setResponse(Response rp){this.response = rp;}
	public Response getResponse(){return response;}

	public final static EventFactory<Context> EVENT_FACTORY = new EventFactory<Context>() {
        public Context newInstance() {
            return new Context();
        }
    };
}
