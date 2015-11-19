package com.creative.service;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.creative.OrderLinkedList;
import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorEvent;
import com.creative.disruptor.DisruptorHandler;
import com.creative.server.ClientHandler;

public class TimerCommandService implements GeneralService {
	private ExecutorService exec;
	public TimerCommandService(){
		//init checkTimer
		this.exec = Executors.newFixedThreadPool(1);
		exec.execute(new Runnable() {
			
			@Override
			public void run() {
				checkTimer();
				
			}
		});
	}
	final static Logger logger = Logger.getLogger(TimerCommandService.class);
	final static String STATE = "STATE";
	final static String TIME_FIRE = "TIME_FIRE";
	final static String TIMER_ID = "TIMER_ID";
	final static String REPEATLY = "REPEATLY";
	final static String REPEAT_HOURLY = "REPEAT_HOURLY";
	final static String REPEAT_DAILY = "REPEAT_DAILY";
	final static String REPEAT_WEEKLY = "REPEAT_WEEKLY";
	final static String REPEAT_NONE = "REPEAT_NONE";
	OrderLinkedList<TimerCommand> queue = new OrderLinkedList<TimerCommand>();
	@Override
	public void onEvent(DisruptorEvent event) throws Exception {
		//{FROM:XXX;COMMAND:TIMER_XXX;TO:XXX;STATE:xxx;TIME_FIRE:XXXX;REPEATLY:XXXX}}
		Context context = event.context;
		IData request = context.getRequest();
		String command;
		try{
			command = request.get(COMMAND);
		}catch(JSONException e){
			return;
		}
		if(!canHandle(command)) return;
		String result = "";
		switch(command){
		case "TIMER_EDIT":
			//edit a timer
			break;
		case "TIMER_SET":
			String commandToFire = StateService.createSetStateCommand(request.get(FROM), 
					request.get(TO), 
					request.get(STATE));
			TimerCommand tc = new TimerCommand(commandToFire, 
					request.get(TIME_FIRE), 
					RepeatType.getRepeatByString(request.get(REPEATLY)));
			queue.add(tc);
			result = "{ID:" + tc.getId() + "}";
			break;
		case "TIMER_REMOVE":
			//delete a timer
			break;
		case "TIMER_LIST":
			//list all timers of a node
			break;
		}
		if(result == null || "".equals(result)) result = "{}";
		context.setResponse(result);
	}

	@Override
	public boolean canHandle(String command) {
		if(command == null || "".equals(command)) return false;
		return command.startsWith("TIMER_");
	}
	private void checkTimer(){
		while(true){
			try {
				Thread.sleep(500);
				TimerCommand.updateCurrent();
				if(queue.getHead().getRemainTime() <= 0) {
					while(queue.getSize() > 0 && queue.getHead().getRemainTime() <=0){
						TimerCommand comm = queue.removeHead();
						logger.debug("Set STATE command: " + comm.getCommand());
						ClientHandler.disrupt.push(new Context(null,comm.getCommand()));
						comm.updateNextTime();
						if(comm.getRemainTime() > 0) {
							queue.add(comm);
							logger.debug("Readd to queue and fire after " + comm.getRemainTime() + "ms");
						}
						
					}
				}
			} catch (InterruptedException e) {
				break;
			}

		}
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
