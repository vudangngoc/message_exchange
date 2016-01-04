package com.creative.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.creative.OrderLinkedList;
import com.creative.context.Context;
import com.creative.server.ClientHandler;
import com.creative.server.TCPServer;

import redis.clients.jedis.Jedis;

public class TimerCommandWDT extends Thread{
	private OrderLinkedList<TimerCommand> queue;
	public TimerCommandWDT(OrderLinkedList<TimerCommand> queue){
		this.queue = queue;
		logger.setLevel(Level.DEBUG);
	}
	final static Logger logger = Logger.getLogger(TimerCommandWDT.class);
	@Override
	public void run(){
		while(true){
			try {
				Thread.sleep(500);
				if(queue.getHead() == null) continue;
				TimerCommand.updateCurrent(); //Always call before working with TimeCommand
				if(queue.getHead().getRemainTime() <= 0) {
					while(queue.getSize() > 0 && queue.getHead().getRemainTime() <=0){
						TimerCommand comm = queue.removeHead();
						logger.debug("Processing timer: " + comm.getId());
						ClientHandler.disrupt.push(new Context(null,comm.getCommand()));
						comm.updateNextTime();
						long remain = comm.getRemainTime();
						if(remain >= 0 && queue.add(comm)) {
							logger.debug("Readd " + comm.getId() + " to queue and fire after " + remain + "ms");
						}else{
							Jedis redisServer = TCPServer.redisPool.getResource();
							redisServer.hdel(TimerCommandService.HASH_NAME, comm.getId());
							logger.debug(comm.getId() + " out of time or there is something wrong: " + remain + "ms");
						}
					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
