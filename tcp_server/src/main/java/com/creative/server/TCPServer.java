package com.creative.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.creative.context.JsonData;
import com.creative.service.GeneralService;
import com.creative.service.StateService;

public class TCPServer {

	public static void main(String[] args) {
		GeneralService service = new StateService();
		try {
			ServerSocket listenSocket = new ServerSocket(10001);
			System.out.println("Start server");
			while(true)          {             
				Socket clientSocket = listenSocket.accept();
				BufferedReader inBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String message = inBuffer.readLine();
				service.processMessage(clientSocket, new JsonData(message));

				System.out.println("Received: " + message);
				IncommingConnectionHandler handler = new IncommingConnectionHandler(clientSocket,service);
				handler.run();
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}  finally{
			
		}
	}

}
