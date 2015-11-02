package com.creative.service;

import java.util.Comparator;
import java.util.PriorityQueue;
import com.creative.disruptor.DisruptorEvent;

public class TimerCommandService implements GeneralService {
	PriorityQueue<TimerCommand> queue = new PriorityQueue<TimerCommand>(100,new Comparator<TimerCommand>() {
		@Override
		public int compare(TimerCommand o1, TimerCommand o2) {
			return (int) (o1.getLifeTime() - o2.getLifeTime());
		}
	});
	@Override
	public void onEvent(DisruptorEvent arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canHandle(String command) {
		// TODO Auto-generated method stub
		return false;
	}
	private void checkTimer(){
		while(true){
			if(queue.size() < 1) break;
			try {
				Thread.sleep(1000);
				TimerCommand.setNow(System.currentTimeMillis());
				if(queue.peek().getLifeTime() <= 0) {
					while(queue.size() > 0 && queue.peek().getLifeTime() <=0){
						System.out.println("Poll item" + queue.poll().command);
					}
				}
			} catch (InterruptedException e) {
				break;
			}
			
		}
	}
}
