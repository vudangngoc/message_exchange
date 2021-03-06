package com.creative.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.creative.GeneralService;
import com.creative.OrderLinkedList;
import com.creative.context.Context;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorEvent;
import com.creative.server.TCPServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TimerCommandService implements GeneralService {
	public TimerCommandService(){
		//init checkTimer
	  TimerCommand.updateCurrent();
		logger.setLevel(Level.INFO);
	}
	/**
	 * Call after setup Disruptor
	 */
	public void startWDT(){
		Thread threadWDT = new Thread(new TimerCommandWDT(queue)); 
		threadWDT.start();
	}
	/**
	 * Dont call at unit test
	 */
	public void startUpdateDB(){
		Thread threadDB = new Thread(new TimerCommandUpdateDB(queue));
		threadDB.start();
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
	public static final String HASH_NAME = "TimerCommand";
	public JedisPool redisPool = TCPServer.redisPool;
	private OrderLinkedList<TimerCommand> queue = new OrderLinkedList<TimerCommand>();

	private TimerCommand editTimeCommand(IData request, TimerCommand origin){
		if(origin == null || request == null) return null;
		TimerCommand result = new TimerCommand();
		IData oldCommand = DataObjectFactory.createDataObject(origin.getCommand());
		String commandToFire = StateService.createSetStateCommand(oldCommand.get(FROM), 
				oldCommand.get(TO), 
				request.get(STATE));
		result.setCommand(commandToFire);
		result.setRepeatType(RepeatType.valueOf(request.get(REPEATLY)));
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		try {
			result.setNextRiseTime(df.parse(request.get(TIME_FIRE)).getTime());
		} catch (ParseException e) {

			return null;
		}
		return result;
	}

	@Override
	public boolean canHandle(String command) {
		if(command == null || "".equals(command)) return false;
		return command.startsWith("TIMER_");
	}

	public static String convertString(TimerCommand timer){
		if(timer == null) return "";
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		IData data = DataObjectFactory.createDataObject();
		data.set(TIMER_ID, timer.getId());
		data.set(COMMAND, timer.getCommand());
		data.set(TIME_FIRE, df.format(new Date(timer.getNextRiseTime())));
		data.set(REPEATLY, timer.getRepeatType().toString());
		return data.toString();
	}
	
	public static TimerCommand revertString(String timer){
	  if(timer == null) return null;
		IData data = DataObjectFactory.createDataObject(timer);
		DateFormat df = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		TimerCommand result = new TimerCommand();
		result.setId(data.get(TIMER_ID));
		result.setCommand(data.get(COMMAND));
		try {
			result.setNextRiseTime(df.parse(data.get(TIME_FIRE)).getTime());
		} catch (ParseException e) {
			result.setNextRiseTime(0);
		}
		result.setRepeatType(RepeatType.getRepeatByString(data.get(REPEATLY)));
		return result;
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

	public static String createAddTimeCommand(String from, String to, String repeat, String time, String state){
		IData data = DataObjectFactory.createDataObject();
		data.set(COMMAND, "TIMER_SET");
		data.set(FROM, from);
		data.set(TO, to);
		data.set(STATE, state);
		data.set(REPEATLY, repeat);
		data.set(TIME_FIRE, time + "");
		return data.toString();
	}
	
	public static String createEditTimeCommand(String id, String repeat, String time, String state){
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

	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
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
			TimerCommand temp = new TimerCommand();
			Jedis redisServer = redisPool.getResource();
			switch(command){
			case "TIMER_EDIT":
				temp.setId(request.get(TIMER_ID));
				temp = queue.getAndRemoveSimilar(temp);
				if(temp != null){
					TimerCommand editResult = editTimeCommand(request, temp);
					if(editResult != null){
						queue.add(editResult);
						result = convertString(editResult);
						redisServer.hset(HASH_NAME, editResult.getId(), result);
						redisServer.hdel(HASH_NAME, request.get(TIMER_ID));
					}
				} else
					logger.debug("Get and remove but not found " + request.get(TIMER_ID));
				break;			
			case "TIMER_SET":
				String commandToFire = StateService.createSetStateCommand(request.get(FROM), 
						request.get(TO), 
						request.get(STATE));
				temp = new TimerCommand(commandToFire,
						request.get(TIME_FIRE), 
						RepeatType.getRepeatByString(request.get(REPEATLY)));
				queue.add(temp);
				result = "{\""+ TIMER_ID +"\":\"" + temp.getId() + "\"}";
				redisServer.hset(HASH_NAME, temp.getId(), convertString(temp));
				break;
			case "TIMER_REMOVE":
				//delete a timer
				temp.setId(request.get(TIMER_ID));
				temp = queue.getAndRemoveSimilar(temp);
				if(temp != null) result = convertString(temp);
				if(redisServer.hdel(HASH_NAME, temp.getId()) == 0)
					logger.debug("Remove but not found " + request.get(TIMER_ID));;
				break;
			case "TIMER_REMOVE_ALL":
				//delete all timer
				queue.removeAll();
				redisServer.del(HASH_NAME);
				break;
			case "TIMER_LIST":
				result = getStatus();
				break;
			}
			if(result == null || "".equals(result)) result = "{}";
			redisServer.close();
			context.setResponse(result);
	}
}
