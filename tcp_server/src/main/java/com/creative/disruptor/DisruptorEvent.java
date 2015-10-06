package com.creative.disruptor;

import com.creative.context.Context;
import com.lmax.disruptor.EventFactory;

public class DisruptorEvent {
	public final static EventFactory<DisruptorEvent> EVENT_FACTORY = new EventFactory<DisruptorEvent>() {
		public DisruptorEvent newInstance() {
			return new DisruptorEvent();
		}
	};
	public Context context;
}
