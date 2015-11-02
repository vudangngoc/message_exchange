package com.creative.service;

public class TimerCommand {
	private static long now;
	public String command;
	public TimerCommand(String command, Long lifeTime){
		timeOut = now + lifeTime;
		this.command = command;
	}
	public static void setNow(long time) {
		now = time;
	}
	private long timeOut;
	public long getLifeTime(){
		return this.timeOut - now;
	}
}
