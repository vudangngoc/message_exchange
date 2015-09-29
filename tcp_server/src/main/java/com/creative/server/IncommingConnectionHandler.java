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
	public IncommingConnectionHandler(Socket socket,GeneralService service){
		this.socket = socket;
		this.service = service;
	}

	public void run(){
		while(true)
		try {
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String message = inBuffer.readLine();
			if(service.processMessage(socket, new JsonData(message))){
				System.out.println("Received: " + message);
			} else{
				System.out.println("Cannot handle: " + message);
			}
			//if(outStream != null) outStream.writeBytes(message);  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
