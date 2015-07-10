package com.creative.service;

import java.io.PrintStream;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandle;

public class HTTPService {
	private DisruptorHandle disrupt;
	private StringBuilder builder;
	public HTTPService(){
		super();
		this.builder = new StringBuilder();
		builder.append("<h1>HELLO WORLD</h1>")
				.append("");
	}

	public void onMessage(PrintStream printStream, String path) {
		printStream.println(builder.toString());
		printStream.close();
	}



}
