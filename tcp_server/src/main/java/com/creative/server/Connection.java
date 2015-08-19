package com.creative.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection extends Thread{
	private Socket socket;

	public Connection(Socket socket){
		this.socket = socket;
	}

	public void run(){
		while(true)
		try {
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

			String message = inBuffer.readLine();

			System.out.println("Received: " + message);
			if(outStream != null) outStream.writeBytes(message);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
