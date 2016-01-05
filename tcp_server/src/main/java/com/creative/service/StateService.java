package com.creative.service;

import java.util.ArrayList;
import java.util.List;

import com.creative.DoubleHashMap;
import com.creative.GeneralService;
import com.creative.RedisHashMap;
import com.creative.context.Context;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.disruptor.DisruptorEvent;
import com.creative.server.TCPServer;

import redis.clients.jedis.JedisPool;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class StateService implements GeneralService {
  public StateService(){
  	logger.setLevel(Level.INFO);
  }

  public static final String DATA = "DATA";
  final static Logger logger = Logger.getLogger(StateService.class);
  public JedisPool redisPool = TCPServer.redisPool;
  private RedisHashMap messageList = new RedisHashMap(redisPool.getResource(),"StateService");

  public boolean canHandle(String command) {
    if(command == null || "".equals(command)) return false;
    return command.startsWith("STATE_");
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


	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		Context context = event.context;
    String command;
    command = context.getRequest().get(COMMAND);
    if("".equals(command)) return;
    if(!canHandle(command)) return;
    IData request = context.getRequest();
    String result = "";
    switch(command){
      case "STATE_GET":
        result = messageList.get(request.get(FROM));
        if(result == null || "".equals(result)){
          result = createSetStateCommand(request.get(FROM),request.get(TO),request.get(DATA));
          messageList.put(request.get(TO), result);
          logger.info("Device " + request.get(TO) + " connect the first time");
        }
        break;
      case "STATE_INFO":
        result = getInfo();
        break;
      case "STATE_STATUS":
        result = getStatus();
        break;
      case "STATE_SET":
        messageList.put(request.get(TO), request.toString());
        break;
    }
    if(result == null || "".equals(result)) {
      IData data = DataObjectFactory.createDataObject();
      data.set("STATE", "OK");
      result = data.toString();
      }
    logger.debug("Processing " + request.toString());
    context.setResponse(result);
		
	}
}
