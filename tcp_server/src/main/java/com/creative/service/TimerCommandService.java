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
	PriorityQueue<TimerCommand> queue = new PriorityQueue<TimerCommand>(100,new Comparator<TimerCommand>() {
		@Override
		public int compare(TimerCommand o1, TimerCommand o2) {
			return (int) (o1.getLifeTime() - o2.getLifeTime());
		}
	});
	@Override
	public void onEvent(DisruptorEvent event) throws Exception {
		Context context = event.context;
		String command;
		try{
			command = context.getRequest().get(GeneralService.COMMAND);
		}catch(JSONException e){
			return;
		}
		if(!canHandle(command)) return;
		String result = "";
		switch(command){

		case "TIMER_SET":
			result = getInfo();
			break;
		case "TIMER_REMOVE":
			result = getInfo();
			break;
		}
		if(result == null || "".equals(result)) result = "{COMMAND:STATE_SET;FROM:nil;TO:"+ context.getRequest().get(GeneralService.FROM) +";DATA:nil}";
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
