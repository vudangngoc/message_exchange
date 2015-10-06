package com.creative.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.creative.service.GeneralService;
import com.creative.service.StateService;

public class TCPServer {

	public static void main(String[] args) {
		try {
			ServerSocket listenSocket = new ServerSocket(10001);
			System.out.println("Start server");
			while(true){             
				Socket clientSocket = listenSocket.accept();
				ClientHandler handler = new ClientHandler(clientSocket);
				handler.run();
			}
		}catch(Exception e){}
		finally{}
	}

}
