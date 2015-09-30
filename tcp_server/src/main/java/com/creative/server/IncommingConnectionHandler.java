package com.creative.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.creative.context.JsonData;
import com.creative.service.GeneralService;
import com.creative.service.StateService;

public class IncommingConnectionHandler extends Thread{
	private Socket socket;
	private GeneralService service;
	private JsonData data = new JsonData("{}");
	public IncommingConnectionHandler(Socket socket,GeneralService service){
		this.socket = socket;
		this.service = service;
	}
	
	public void run(){
		try {
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message;
			while(true){
				message = inBuffer.readLine();
				if(message != null && data.setData(message)){
					if(service.processMessage(socket, data)){
						System.out.println("Received: " + message);
					} else{
						System.out.println("Cannot handle: " + message);
					}
				}else{
					System.out.println("Syntax error: " + message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(!socket.isClosed())
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
}
