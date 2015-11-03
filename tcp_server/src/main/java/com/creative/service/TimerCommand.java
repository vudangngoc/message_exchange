package com.creative.service;

import java.util.UUID;

public class TimerCommand {
	private static long now;
	public String command;
	public TimerCommand(String command, Long lifeTime){
		timeOut = now + lifeTime;
		this.command = command;
		this.id = UUID.randomUUID().toString();
	}
	public static void setNow(long time) {
		now = time;
	}
	private long timeOut;
	private String id;
	
	public long getLifeTime(){
		return this.timeOut - now;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setExpireTime(long time){
		this.timeOut = time;
	}
	
	public void setRemainTime(long time){
		this.timeOut = now + time;
	}
}
