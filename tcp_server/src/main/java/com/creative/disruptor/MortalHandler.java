package com.creative.disruptor;



import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class MortalHandler implements EventHandler<DisruptorEvent> {
	public MortalHandler(Class<? extends WorkHandler<DisruptorEvent>> clazz, int lifeTime) throws InstantiationException, IllegalAccessException{
		this.clazz = clazz;
		LIFE_TIME = lifeTime;
		RefreshWorker();
	}
	private int LIFE_TIME = 1024;
	private WorkHandler<DisruptorEvent> handler;
	private Class<? extends WorkHandler<DisruptorEvent>> clazz;
	int lifeTime = LIFE_TIME;
	public void onEvent(DisruptorEvent arg0) throws Exception {
	
	}

	private void ReduceLifeTime() {
		this.lifeTime -= 1;
		if(lifeTime < 1)
			try {
				RefreshWorker();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void RefreshWorker() throws InstantiationException, IllegalAccessException {
		handler = clazz.newInstance();
		lifeTime = LIFE_TIME;
	}

	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) throws Exception {
		handler.onEvent(event);
		this.ReduceLifeTime();	
	}
}
