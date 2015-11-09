package com.creative.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
/**
 * TimerCommand should aware current system time, but it require call
 * System.currentTimeMillis() and cause big trouble if there are a lot of
 * TimerCommand instance. So that be sure re-asign {@link TimerCommand.now}
 * each time using instance of TimerCommand
 * @author vudangngoc@gmail.com
 *
 */
public class TimerCommand implements Comparable<TimerCommand>{
	private static long now;
	private static Calendar calendar = Calendar.getInstance();
	private static final String TIME_FORMAT= "yyyy-MM-dd HH:mm:ss z";
	/**
	 * Call before work with multi instances of TimeCommand
	 */
	public static void updateCurrent(){
		now = System.currentTimeMillis();
		calendar.setTime(new Date());
	}
	public String command;
	public TimerCommand(String command, String timeConfig, RepeatType repeatType){
		this.setTimeConfig(timeConfig);
		this.command = command;
		this.repeatType = repeatType;
		this.id = UUID.randomUUID().toString();
	}
	public void updateNextTime(){
		if(this.repeatType == RepeatType.REPEAT_NONE) this.nextRiseTime = 0;
	}
	public static void setNow(long time) {
		now = time;
	}
	private long nextRiseTime;
	private String id;
	private RepeatType repeatType;
	private Calendar timeConfig = Calendar.getInstance();
	public Calendar getTimeConfig() {
		return timeConfig;
	}
	public void setTimeConfig(String timeConfig) {
		DateFormat formatter = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
		try {
			Date date = formatter.parse(timeConfig);
			this.timeConfig.setTime(date);
		} catch (ParseException e) {
			
		}
		
	}

	public long getRemainTime(){
		return this.nextRiseTime - now;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setNextRiseTime(long time){
		this.nextRiseTime = time;
	}
	
	public void setRemainTime(long time){
		this.nextRiseTime = now + time;
	}
	@Override
	public boolean equals(Object e){
		if(e instanceof TimerCommand){
			return this.id.equals(((TimerCommand)e).id);
		}else
			return false;
	}

	@Override
	public int compareTo(TimerCommand o) {
		return (int)(this.nextRiseTime - o.nextRiseTime);
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
