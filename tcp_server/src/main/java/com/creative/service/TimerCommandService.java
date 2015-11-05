package com.creative.service;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorEvent;
import com.creative.disruptor.DisruptorHandler;
import com.creative.server.ClientHandler;

public class TimerCommandService implements GeneralService {
	public TimerCommandService(){
		//init checkTimer
	}
	final static Logger logger = Logger.getLogger(TimerCommandService.class);
	final static String STATE = "STATE";
	final static String TIME_FIRE = "TIME_FIRE";
	final static String TIMER_ID = "TIMER_ID";
	final static String REPEATLY = "REPEATLY";
	final static String REPEAT_HOURLY = "REPEAT_HOURLY";
	final static String REPEAT_DAILY = "REPEAT_DAILY";
	final static String REPEAT_WEEKLY = "REPEAT_WEEKLY";
	final static String REPEAT_MONTHLY = "REPEAT_MONTHLY";
	final static String REPEAT_NONE = "REPEAT_NONE";
	PriorityQueue<TimerCommand> queue = new PriorityQueue<TimerCommand>(100,new Comparator<TimerCommand>() {
		@Override
		public int compare(TimerCommand o1, TimerCommand o2) {
			return (int) (o1.getLifeTime() - o2.getLifeTime());
		}
	});
	@Override
	public void onEvent(DisruptorEvent event) throws Exception {
		//{FROM:XXX;COMMAND:TIMER_XXX;TO:XXX;STATE:xxx;TIME_FIRE:XXXX;REPEATLY:XXXX}}
		Context context = event.context;
		String command;
		try{
			command = context.getRequest().get(COMMAND);
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
			//create a timer
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
			if(queue.size() < 1) break;
			try {
				Thread.sleep(1000);
				TimerCommand.setNow(System.currentTimeMillis());
				if(queue.peek().getLifeTime() <= 0) {
					while(queue.size() > 0 && queue.peek().getLifeTime() <=0){
						ClientHandler.disrupt.push(new Context(null,queue.poll().command));
						logger.info("Set timmer" + queue.poll().command);
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
