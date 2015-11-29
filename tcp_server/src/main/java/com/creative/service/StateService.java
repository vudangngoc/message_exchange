package com.creative.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorEvent;

import org.apache.log4j.Logger;

public class StateService implements GeneralService {
  public StateService(){
  }

  public static final String DATA = "DATA";
  final static Logger logger = Logger.getLogger(StateService.class);

  ConcurrentHashMap <String,String> messageList = new ConcurrentHashMap<String, String>();

  public boolean canHandle(String command) {
    if(command == null || "".equals(command)) return false;
    return command.startsWith("STATE_");
  }

  @Override
  public void onEvent(DisruptorEvent event) throws Exception {
    Context context = event.context;
    String command;
    command = context.getRequest().get(COMMAND);
    if("".equals(command)) return;
    if(!canHandle(command)) return;
    String result = "";
    switch(command){
      case "STATE_GET":
        result = messageList.get(context.getRequest().get(FROM));
        break;
      case "STATE_INFO":
        result = getInfo();
        break;
      case "STATE_STATUS":
        result = getStatus();
        break;
      case "STATE_SET":
        messageList.put(context.getRequest().get(TO), context.getRequest().toString());
        IData data = DataObjectFactory.createDataObject();
        data.set("STATE", "OK");
        result = data.toString();
        break;
    }
    if(result == null || "".equals(result)) result = "{COMMAND:STATE_SET;FROM:nil;TO:"+ context.getRequest().get(FROM) +";DATA:nil}";
    logger.debug("Processing " + context.getRequest().toString());
    context.setResponse(result);

  }

  @Override
  public String getStatus() {
    List<String> list = new ArrayList<>();
    for(String s :messageList.values())
      list.add(s);
    IData data = DataObjectFactory.createDataObject();
    data.setList("data", list);
    return data.toString();
  }

  @Override
  public String getInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  final public static String createSetStateCommand(String from, String to, String state){
    IData data = DataObjectFactory.createDataObject();
    data.set(COMMAND, "STATE_SET");
    data.set(FROM, from);
    data.set(TO, to);
    data.set(DATA, state);

    return data.toString();
  }

  public static String createGetStatus() {
    IData data = DataObjectFactory.createDataObject();
    data.set(COMMAND, "STATE_STATUS");
    return data.toString();
  }
}
