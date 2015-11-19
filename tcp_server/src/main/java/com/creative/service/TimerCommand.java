package com.creative.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
/**
 * TimerCommand should aware current system time, but it require call
 * System.currentTimeMillis() and cause big trouble if there are a lot of
 * TimerCommand instance. So that be sure re-asign {@link TimerCommand.now}
 * each time using instance of TimerCommand
 * @author vudangngoc@gmail.com
 *
 */
public class TimerCommand implements Comparable<TimerCommand>{
  final static Logger logger = Logger.getLogger(TimerCommand.class);
  private static long now;
  private static final String TIME_FORMAT= "yyyy-MMM-dd HH-mm-ss Z";
  /**
   * Call before work with multi instances of TimeCommand
   */
  public static void updateCurrent(){
    now = System.currentTimeMillis();
  }
  private String command;
  private long nextRiseTime;
  private String id;
  private RepeatType repeatType;
  public TimerCommand(){}
  public TimerCommand(String command, String timeConfig, RepeatType repeatType){
    this.setTimeConfig(timeConfig);
    this.command = command;
    this.repeatType = repeatType;
    this.id = UUID.randomUUID().toString();
  }
  public void updateNextTime(){
    if(TimerCommand.now > nextRiseTime){ //Prevent call this function many times
      nextRiseTime += RepeatType.getRepeatDuration(repeatType);
    }
  }

  public boolean setTimeConfig(String timeConfig) {
    DateFormat formatter = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
    try {
      Date date = formatter.parse(timeConfig);
      this.nextRiseTime = date.getTime();
      return true;
    } catch (ParseException e) {
      logger.debug("Parse Date faile ",e);
      return false;
    }		
  }

  public long getRemainTime(){
    return this.nextRiseTime - now;
  }

  public String getId(){
    return this.id;
  }

  public void setId(String id){
    this.id = id;
  }

  public void setNextRiseTime(long time){
    this.nextRiseTime = time;
  }

  public long getNextRiseTime(){
    return this.nextRiseTime;
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
  public RepeatType getRepeatType() {
    return repeatType;
  }
  public void setRepeatType(RepeatType repeatType) {
    this.repeatType = repeatType;
  }
}
