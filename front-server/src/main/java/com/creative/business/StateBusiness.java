package com.creative.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.creative.connector.back_server.Connector;
import com.creative.connector.back_server.MockConnector;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.service.StateService;

public class StateBusiness {
  
  private static Connector connector = new MockConnector();
  
  public static Map<String,String> getAllDevice(){
    Map<String,String> result = new HashMap<String,String>();
    String messageResult = connector.sendMessage(StateService.createGetStatus());
    List<String> listResult = DataObjectFactory.createDataObject(messageResult).getList("data");
    IData data = DataObjectFactory.createDataObject();
    for(String s:listResult){
      data.setData(s);
      result.put(data.get(StateService.TO), data.get(StateService.TO));
    }
    return result;
  }
  
  public static String setDeviceState(String name, String state){
    IData data = DataObjectFactory.createDataObject();
    String message = StateService.createSetStateCommand("WEB_UI", name, state);
    data.setData(connector.sendMessage(message));
    return data.get("STATE");
  }
}
