package com.creative.dns;

import java.util.HashMap;

public class WDT extends Thread {
	private HashMap<String, Domain> data;
	public WDT(HashMap<String, Domain> data){
		this.data = data;
	}
	@Override
	public void run(){
		while(true){
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}
}
