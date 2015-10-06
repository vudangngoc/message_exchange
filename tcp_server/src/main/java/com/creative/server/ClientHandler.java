package com.creative.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandler;
import com.creative.disruptor.MortalHandler;
import com.creative.service.GeneralService;
import com.creative.service.StateService;

public class ClientHandler extends Thread{
	private Socket socket;

	public ClientHandler(Socket socket){
		this.socket = socket;
	}

	private static DisruptorHandler disrupt;
	protected static DisruptorHandler getDisruptorHandler() {
		if(disrupt == null) {
			disrupt = new DisruptorHandler(512);
			try {
				disrupt.injectServices(new MortalHandler(StateService.class, 10000));
				disrupt.startDisruptor();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}			
		}
		return disrupt;
	}

	public void run(){
		try {
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message;
			while(true){
				if(socket.isClosed() || inBuffer == null) break;
				message = inBuffer.readLine();
				if(message != null && ClientHandler.getDisruptorHandler() != null){
					ClientHandler.getDisruptorHandler().push(new Context(socket, message));
					System.out.println("Received: " + message);
				}
			}
		}catch (IOException e) {
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
