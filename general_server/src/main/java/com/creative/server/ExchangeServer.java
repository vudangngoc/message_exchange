package com.creative.server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.creative.json.JsonData;
import com.creative.service.HTTPService;
import com.creative.service.StateService;
import com.creative.service.GeneralService;


public class ExchangeServer implements Container {

	private static HTTPService httpService;
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static void main(String[] args) throws Exception{
		
		Container container = new ExchangeServer();
		SocketProcessor server = new ContainerSocketProcessor(container);
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(10001);
		connection.connect(address);
		servicesPool.add(new StateService());
		ExchangeServer.httpService = new HTTPService();
	}
	private static ArrayList<GeneralService> servicesPool = new ArrayList<GeneralService>();
	public void handle(Request request, Response response) {
		Calendar now = Calendar.getInstance();
		System.out.println(dateFormat.format(now.getTime())+";"+request.getPath().getPath());
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
