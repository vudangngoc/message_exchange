package com.creative.server;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import com.creative.disruptor.DisruptorHandle;


public class SimpleServer implements Container {

	public static void main(String[] args) throws Exception{
		Container container = new SimpleServer();
		SocketProcessor server = new ContainerSocketProcessor(container);
		Connection connection = new SocketConnection(server);
		SocketAddress address = new InetSocketAddress(10001);
		connection.connect(address);
	}
	
	private DisruptorHandle disruptor = new DisruptorHandle();
	
	public void handle(Request request, Response response) {
		this.disruptor.push(request, response);
	}

}
