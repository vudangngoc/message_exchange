package com.creative.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.creative.OrderLinkedList;
import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.server.TCPServer;

import redis.clients.jedis.Jedis;

public class TimerCommandUpdateDB extends Thread{
	private OrderLinkedList<TimerCommand> queue;
	public TimerCommandUpdateDB(OrderLinkedList<TimerCommand> queue){
		this.queue = queue;
		logger.setLevel(Level.INFO);
	}
	final static Logger logger = Logger.getLogger(TimerCommandUpdateDB.class);
	@Override
	public void run(){
		while(true){
			try {
				Jedis redisServer = TCPServer.redisPool.getResource();
				List<String> db = new ArrayList<>(redisServer.hkeys(TimerCommandService.HASH_NAME));
				logger.debug("Fetch " + db.size() + " items from Redis server");
				queue.diff(db);
				if(db.size() > 0) {
				  String[] arr = new String[db.size()];
				  List<String> commandToAdd = redisServer.hmget(TimerCommandService.HASH_NAME, db.toArray(arr));
				  logger.debug("Fetch " + commandToAdd.size() + " new items from Redis server");
				  for(String comman : commandToAdd){
				    TimerCommand temp = TimerCommandService.revertString(comman);
				    if("".equals(temp.getId())) continue;
				    while(temp.getNextRiseTime() < System.currentTimeMillis())
				      temp.updateNextTime();
				    queue.add(temp);
				  }
				}
				redisServer.close();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				break;
			} 
		}
	}
}
