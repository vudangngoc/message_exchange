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


public class ExchangeServer implements Container {

	public static void main(String[] args) throws Exception{
		Container container = new ExchangeServer();
		SocketProcessor server = new ContainerSocketProcessor(container);
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(10001);
		connection.connect(address);
	}
	
	public void handle(Request request, Response response) {
		boolean handled = false;
		request.getChannel().getWriter();

	}

}
