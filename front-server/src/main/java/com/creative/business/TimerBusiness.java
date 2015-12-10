package com.creative.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creative.connector.back_server.Connector;
import com.creative.connector.back_server.TCPConnector;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.service.StateService;
import com.creative.service.TimerCommandService;
import com.creative.service.RepeatType;;

public class TimerBusiness {
  private StateBusiness stateBusiness = new StateBusiness();
  private Connector connector = new TCPConnector();
  private List<IData> timerList = new ArrayList<>();
  public TimerBusiness(){
    if(connector instanceof TCPConnector){
      ((TCPConnector)connector).setUp("s1.thietbithongminh.info", 10001);
    }
  }
  
  public void refreshData(){
  	timerList.clear();
  	String messageResult = connector.sendMessage(TimerCommandService.createListTimeCommand());
  	IData data_result = DataObjectFactory.createDataObject(messageResult);
  	
  	if(data_result != null){
  		List<String> listResult = DataObjectFactory.createDataObject(messageResult).getList("data");
  		
  		for(String s:listResult){
  			IData data = DataObjectFactory.createDataObject(s);
  			timerList.add(data);
  		}
  	} 
  }
  
  public List<String> getDevices(){
  	Collection<String> result = stateBusiness.getAllDevice().keySet();
  	List<String> r = new ArrayList<>();
  	result.forEach(x -> r.add(x));
  	return r;
  }
  
  public String getTimers(String deviceName){
  	IData result = DataObjectFactory.createDataObject();
  	if(deviceName == null) return "{}";
  	for(IData data : timerList){
  		if(deviceName.equals(data.get(TimerCommandService.TO)))
  			result.set(data.get(TimerCommandService.TIMER_ID),data.toString());
  	}
  	return result.toString();
  }
  
  public boolean updateTimer(String id, RepeatType repeat, long time, String state){
  	connector.sendMessage(TimerCommandService.createEditTimeCommand(id, repeat, time, state));
  	return false;
  }
  
  public boolean deleteTimer(String id){
  	connector.sendMessage(TimerCommandService.createRemoveTimeCommand(id));
  	return false;
  }
}
