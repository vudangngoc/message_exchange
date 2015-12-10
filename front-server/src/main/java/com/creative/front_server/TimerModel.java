package com.creative.front_server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.creative.service.RepeatType;

public class TimerModel {
	private List<String> deviceList = new ArrayList<>();
	private String deviceId;
	private String timerId;
	private String state;
	private RepeatType repeatType;
	private String time;
	public List<String> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(List<String> deviceList) {
		this.deviceList = deviceList;
	}
	public HashMap<String, List<String>> getTimerList() {
		return timerList;
	}
	public void setTimerList(HashMap<String, List<String>> timerList) {
		this.timerList = timerList;
	}
	public String getTimerId() {
		return timerId;
	}
	public void setTimerId(String timerId) {
		this.timerId = timerId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public RepeatType getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(RepeatType repeatType) {
		this.repeatType = repeatType;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	private HashMap<String,List<String>> timerList = new HashMap<>();
}
