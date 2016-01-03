package com.creative.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	}
	final static Logger logger = Logger.getLogger(TimerCommandUpdateDB.class);
	@Override
	public void run(){
		while(true){
			try {
				Jedis redisServer = TCPServer.redisPool.getResource();
				List<String> db = new ArrayList<>(redisServer.hkeys(TimerCommandService.HASH_NAME));
				queue.diff(db);
				String[] arr = new String[db.size()];
				List<String> commandToAdd = redisServer.hmget(TimerCommandService.HASH_NAME, db.toArray(arr));
				for(String comman : commandToAdd){
					queue.add(TimerCommandService.revertString(comman));
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			} 
		}
	}
}
