package com.creative.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.creative.context.Context;
import com.creative.disruptor.DisruptorEvent;
import com.creative.disruptor.DisruptorHandler;
import com.creative.disruptor.OnePublisherWorkerPool;
import com.creative.service.StateService;
import com.creative.service.TimerCommandService;
import com.lmax.disruptor.WorkHandler;
import com.creative.GeneralService;
import com.creative.GlobalConfig;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class ClientHandler {

	private static class SocketHandler implements WorkHandler<DisruptorEvent>{

		private boolean isCanHandle(String command) {
			for(GeneralService s : services){
				if(s.canHandle(command)) return true;
			}
			return false;
		}
		@Override
		public void onEvent(DisruptorEvent event) throws Exception {
			Socket socket = event.context.getClient();
			try {
				BufferedReader inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String message;
				if(socket.isClosed() || inBuffer == null) return;
				message = inBuffer.readLine();
				if(message != null && disrupt != null){
					Context context = new Context(socket, message);
					if(isCanHandle(context.getRequest().get(GeneralService.COMMAND))){
						disrupt.push(context);
						logger.debug("Received: " + message);
					}else{
						logger.debug("Cannot handle: " + message);
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

	}

	final static Logger logger = Logger.getLogger(ClientHandler.class);

	public ClientHandler(){
		logger.setLevel(Level.INFO);
		disrupt = new DisruptorHandler(Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.RING_BUFFER_SIZE)));

		StateService stateService = new StateService();
		services.add(stateService);
		disrupt.injectServices(stateService);
		TimerCommandService timerService = new TimerCommandService();
		services.add(timerService);
		disrupt.injectServices(timerService);
		disrupt.startDisruptor();
		timerService.startWDT();
		timerService.startUpdateDB();
		
    //N_threads = N_cpu * U_cpu * (1 + W / C)
    int processors = Runtime.getRuntime().availableProcessors();
    SocketHandler[] arr = new SocketHandler[processors];
    for(int i = 0; i < processors; i++)
      arr[i] = new SocketHandler();
    
    socketPool = new OnePublisherWorkerPool(arr);
	}
	private OnePublisherWorkerPool socketPool;
	
	/**
	 * Check disruptor status before push event
	 */
	public static DisruptorHandler disrupt;
	public static List<GeneralService> services = new ArrayList<GeneralService>();

	public void run(Socket socket){
		socketPool.push(new Context(socket,""));
	}


}
