package com.creative.server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.creative.json.JsonData;
import com.creative.service.HTTPService;
import com.creative.service.ExchangeMessageService;
import com.creative.service.GeneralService;


public class ExchangeServer implements Container {

	private static HTTPService httpService;
	public static void main(String[] args) throws Exception{
		Container container = new ExchangeServer();
		SocketProcessor server = new ContainerSocketProcessor(container);
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(10001);
		connection.connect(address);
		servicesPool.add(new ExchangeMessageService());
		ExchangeServer.httpService = new HTTPService();
	}
	private static ArrayList<GeneralService> servicesPool = new ArrayList<GeneralService>();
	public void handle(Request request, Response response) {
		System.out.println(request.getPath().getPath());
		boolean handled = false;		
		JsonData data;
		try{
			data = new JsonData(request.getPath().getPath().substring(1));
		}catch(Exception e){
			try {
				ExchangeServer.httpService.onMessage(response.getPrintStream(),request.getPath().getPath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}

		for(GeneralService service : servicesPool){
			try { 
				if(service.processMessage(response.getPrintStream(), data)) 
					handled = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!handled) {
			try {
				response.getPrintStream().println("Error 404: Unknow request!");
				response.getPrintStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
