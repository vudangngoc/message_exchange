package com.creative.service;

enum RepeatType {REPEAT_NONE,REPEAT_HOURLY,REPEAT_DAILY,REPEAT_WEEKLY;
	public static long getRepeatDuration(RepeatType repeatType){
		switch (repeatType) {
		case REPEAT_DAILY:
			return 24*60*60*1000;
		case REPEAT_HOURLY:
			return 60*60*1000;
		case REPEAT_WEEKLY:
			return 7*24*60*60*1000;
		default:
			return 0;
		}
	}
	public static RepeatType getRepeatByString(String type){
		switch (type) {
		case "REPEAT_HOURLY":
			return REPEAT_HOURLY;
		case "REPEAT_DAILY":
			return REPEAT_DAILY;
		case "REPEAT_WEEKLY":
			return REPEAT_WEEKLY;
		default:
			return REPEAT_NONE;
		}
	}
}