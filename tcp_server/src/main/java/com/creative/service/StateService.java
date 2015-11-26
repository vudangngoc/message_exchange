package com.creative.service;

import java.util.concurrent.ConcurrentHashMap;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorEvent;

import org.apache.log4j.Logger;
import org.json.JSONException;

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
    try{
      command = context.getRequest().get(COMMAND);
    }catch(JSONException e){
      return;
    }
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
        result = "{STATE:OK}";
        break;
    }
    if(result == null || "".equals(result)) result = "{COMMAND:STATE_SET;FROM:nil;TO:"+ context.getRequest().get(FROM) +";DATA:nil}";
    logger.debug("Processing " + context.getRequest().toString());
    context.setResponse(result);

  }

  @Override
  public String getStatus() {
    StringBuffer result = new StringBuffer();
    result.append("{");
    for(String s : messageList.values()){
      result.append(s).append(";");
    }
    result.append("}");
    return result.toString();
  }

  @Override
  public String getInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  final public static String createSetStateCommand(String from, String to, String state){
    String result = "{COMMAND:STATE_SET;FROM:" + from + ";TO:" + to + ";DATA:" + state + "}";

    return result;
  }
}
