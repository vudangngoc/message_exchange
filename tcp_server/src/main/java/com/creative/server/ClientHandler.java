package com.creative.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandler;
import com.creative.disruptor.MortalHandler;
import com.creative.service.GeneralService;
import com.creative.service.StateService;
import com.creative.service.TimerCommandService;
import com.creative.GlobalConfig;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class ClientHandler extends Thread{

	final static Logger logger = Logger.getLogger(ClientHandler.class);

	private Socket socket;

	public ClientHandler(Socket socket){
		this.socket = socket;
	}

	public static DisruptorHandler disrupt;
	private static List<MortalHandler> services = new ArrayList<MortalHandler>();
	protected static DisruptorHandler getDisruptorHandler() {
		if(disrupt == null) {
			disrupt = new DisruptorHandler(Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.RING_BUFFER_SIZE)));
			try {
				MortalHandler service = new MortalHandler(StateService.class,
						Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.WORKER_LIFE_TIME)));
				services.add(service);
				disrupt.injectServices(service);
				MortalHandler timerService = new MortalHandler(TimerCommandService.class,
						Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.WORKER_LIFE_TIME)));
				services.add(timerService);
				disrupt.injectServices(timerService);
				disrupt.startDisruptor();
			} catch (InstantiationException | IllegalAccessException e) {
				logger.debug(e);
			}
		}
		return disrupt;
	}

	public void run(){
		try {
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message;
			if(socket.isClosed() || inBuffer == null) return;
			message = inBuffer.readLine();
			if(message != null && ClientHandler.getDisruptorHandler() != null){
				Context context = new Context(socket, message);
				if(isCanHandle(context.getRequest().get(GeneralService.COMMAND))){
					ClientHandler.getDisruptorHandler().push(context);
					logger.info("Received: " + message);
				}else{
					logger.info("Cannot handle: " + message);
					try {
						socket.close();
					} catch (IOException e) {
						logger.debug(e);
					}
				}
			}
		}catch (IOException | JSONException e) {
			logger.debug(e);
			try {
				socket.close();
			} catch (IOException ex) {
				logger.debug(ex);
			}
		} 
	}

	private boolean isCanHandle(String command) {
		for(MortalHandler s : services){
			if(s.canHandle(command)) return true;
		}
		return false;
	}
}
