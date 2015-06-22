package com.creative.server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.creative.disruptor.DisruptorHandle;
import com.creative.json.JsonData;
import com.creative.service.EchoService;
import com.creative.service.GeneralService;


public class SimpleServer implements Container {

	public static void main(String[] args) throws Exception{
		Container container = new SimpleServer();
		SocketProcessor server = new ContainerSocketProcessor(container);
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(10001);
		connection.connect(address);
		servicesPool.add(new EchoService());
	}
	private static ArrayList<GeneralService> servicesPool = new ArrayList<GeneralService>();
	public void handle(Request request, Response response) {
		for(GeneralService service : servicesPool){
			try {
				JsonData data = new JsonData(request.getPath().getPath().substring(1));
				service.processMessage(response.getPrintStream(), data);
				response.getPrintStream().println(data.get("COMMAND"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*try {
				
				service.processMessage(response.getPrintStream(), data);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		try {
			response.getPrintStream().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
