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
import org.apache.log4j.Logger;

public class ClientHandler extends Thread{

	final static Logger logger = Logger.getLogger(ClientHandler.class);

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
				if(logger.isDebugEnabled()){
	    logger.debug(e);
			}
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
					if(logger.isInfoEnabled()){
			logger.info("Received: " + message);
		}
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
