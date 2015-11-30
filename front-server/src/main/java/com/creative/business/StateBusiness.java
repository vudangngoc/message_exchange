package com.creative.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.creative.connector.back_server.Connector;
import com.creative.connector.back_server.MockConnector;
import com.creative.connector.back_server.TCPConnector;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.service.StateService;

public class StateBusiness {
  
  private Connector connector = new TCPConnector();
  
  public StateBusiness(){
    if(connector instanceof TCPConnector){
      ((TCPConnector)connector).setUp("127.0.0.1", 10001);
    }
  }
  
  public Map<String,String> getAllDevice(){
    Map<String,String> result = new HashMap<String,String>();
    String messageResult = connector.sendMessage(StateService.createGetStatus());
    List<String> listResult = DataObjectFactory.createDataObject(messageResult).getList("data");
    IData data = DataObjectFactory.createDataObject();
    for(String s:listResult){
      data.setData(s);
      result.put(data.get(StateService.TO), data.toString());
    }
    return result;
  }
  
  public String setDeviceState(String name, String state){
    IData data = DataObjectFactory.createDataObject();
    String message = StateService.createSetStateCommand("WEB_UI", name, state);
    data.setData(connector.sendMessage(message));
    return data.get("STATE");
  }
}
