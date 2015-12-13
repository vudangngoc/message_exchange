package com.creative.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

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
      ((TCPConnector)connector).setUp("127.0.0.1", 10001);
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
  	refreshData();
  	IData result = DataObjectFactory.createDataObject();
  	if(deviceName == null) return "{}";
  	List<String> list = new ArrayList<>();
  	for(IData data : timerList){
  		IData command = DataObjectFactory.createDataObject(data.get(TimerCommandService.COMMAND));
  		if(deviceName.equals(command.get(TimerCommandService.TO)))
  			list.add(data.toString());
  	}
  	result.setList("data", list);
  	return result.toString();
  }
  
  public boolean updateTimer(String id, String repeat, String time, String state){
  	connector.sendMessage(TimerCommandService.createEditTimeCommand(id, repeat, time, state));
  	return false;
  }
  
  public boolean deleteTimer(String id){
  	connector.sendMessage(TimerCommandService.createRemoveTimeCommand(id));
  	return false;
  }
  public boolean createTimer(String from, String to, String repeat, String time, String state){
  	String message = TimerCommandService.createAddTimeCommand(from, to, repeat, time, state);
  	connector.sendMessage(message);
  	return false;
  }

	public String getTimer(String id) {
		for(IData data : timerList){
			if(data.get("TIMER_ID").equals(id))
				return data.toString();
		}
		return null;
	}
}
