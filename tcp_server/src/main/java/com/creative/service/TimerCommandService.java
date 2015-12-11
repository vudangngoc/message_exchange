package com.creative.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.creative.OrderLinkedList;
import com.creative.context.Context;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorEvent;
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
	public final static String STATE = "STATE";
	public final static String TIME_FIRE = "TIME_FIRE";
	public final static String TIMER_ID = "TIMER_ID";
	public final static String REPEATLY = "REPEATLY";
	public final static String REPEAT_MINUTELY = "REPEAT_MINUTELY";
	public final static String REPEAT_HOURLY = "REPEAT_HOURLY";
	public final static String REPEAT_DAILY = "REPEAT_DAILY";
	public final static String REPEAT_WEEKLY = "REPEAT_WEEKLY";
	public final static String REPEAT_NONE = "REPEAT_NONE";
	static OrderLinkedList<TimerCommand> queue = new OrderLinkedList<TimerCommand>();
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
		String commandToFire;
		switch(command){
		case "TIMER_EDIT":
			TimerCommand tc_edit = new TimerCommand();
			tc_edit.setId(request.get(TIMER_ID));
			tc_edit = queue.getAndRemoveSimilar(tc_edit);
			commandToFire = StateService.createSetStateCommand(request.get(FROM), 
					request.get(TO), 
					request.get(STATE));
			tc_edit.setCommand(commandToFire);
			tc_edit.setRepeatType(RepeatType.valueOf(request.get(REPEATLY)));
			tc_edit.setNextRiseTime(Long.parseLong(request.get(TIME_FIRE)));
			queue.add(tc_edit);
			break;
		case "TIMER_SET":
			commandToFire = StateService.createSetStateCommand(request.get(FROM), 
					request.get(TO), 
					request.get(STATE));
			DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
			long timeMili = Long.parseLong(request.get(TIME_FIRE));
			String time = df.format(new Date(timeMili));
			TimerCommand tc = new TimerCommand(commandToFire,
					time, 
					RepeatType.getRepeatByString(request.get(REPEATLY)));
			queue.add(tc);
			result = "{"+ TIMER_ID +":" + tc.getId() + "}";
			break;
		case "TIMER_REMOVE":
			//delete a timer
			TimerCommand temp = new TimerCommand();
			temp.setId(request.get(TIMER_ID));
			temp = queue.getAndRemoveSimilar(temp);
			if(temp != null) result = convertString(temp);
			else result = "{}";
			break;
		case "TIMER_REMOVE_ALL":
			//delete all timer
			queue.removeAll();
			break;
		case "TIMER_LIST":
			result = getStatus();
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
				if(queue.getHead() == null) continue;
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

	public String convertString(TimerCommand timer){
		IData data = DataObjectFactory.createDataObject();
		data.set(TIMER_ID, timer.getId());
		data.set(COMMAND, timer.getCommand());
		data.set(TIME_FIRE, timer.getNextRiseTime() + "");
		return data.toString();
	}
	@Override
	public String getStatus() {
		List<TimerCommand> list = queue.getAll();
		List<String> listString = new ArrayList<>();
		for(TimerCommand tc : list)
			listString.add(convertString(tc));
		IData data = DataObjectFactory.createDataObject();
		data.setList("data", listString);
		return data.toString();
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String createAddTimeCommand(String from, String to, String repeat, long time, String state){
		IData data = DataObjectFactory.createDataObject();
		data.set(COMMAND, "TIMER_SET");
		data.set(FROM, from);
		data.set(TO, to);
		data.set(STATE, state);
		data.set(REPEATLY, repeat);
		data.set(TIME_FIRE, time + "");

		return data.toString();
	}
	public static String createEditTimeCommand(String id, String repeat, long time, String state){
		IData data = DataObjectFactory.createDataObject();
		data.set(COMMAND, "TIMER_EDIT");
		data.set(TIMER_ID, id);
		data.set(STATE, state);
		data.set(REPEATLY, repeat);
		data.set(TIME_FIRE, time + "");

		return data.toString();
	}
	public static String createRemoveTimeCommand(String id){
		IData data = DataObjectFactory.createDataObject();
		data.set(COMMAND, "TIMER_REMOVE");
		data.set(TIMER_ID, id);

		return data.toString();
	}
	public static String createListTimeCommand(){
		IData data = DataObjectFactory.createDataObject();
		data.set(COMMAND, "TIMER_LIST");

		return data.toString();
	}
}
