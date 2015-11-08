package com.creative.service;

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
	private static Calendar calendar = Calendar.getInstance();;
	/**
	 * Call before work with multi instances of TimeCommand
	 */
	public static void updateCurrent(){
		now = System.currentTimeMillis();
		calendar.setTime(new Date());
	}
	public String command;
	public TimerCommand(String command, String timeConfig){
		this.timeConfig = timeConfig;
		this.command = command;
		this.id = UUID.randomUUID().toString();
	}
	public static void setNow(long time) {
		now = time;
	}
	private long nextRiseTime;
	private String id;
	private String timeConfig;
	private int minute;
	private int hour;
	private int dayOfMonth;
	private int month;
	private int dayOfWeek;
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
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
