package com.creative.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer implements GeneralServer {

	public boolean start() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}

	public void bind(String hostName, long port) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		org.simpleframework.transport.connect.SocketConnection s;
		try {
			ServerSocket serverSocket = new ServerSocket(10001);
			StringWriter writer = new StringWriter();
			while(true){
				Socket clientSocket = serverSocket.accept();
				
				System.out.println("");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
