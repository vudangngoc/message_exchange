package com.creative.server;

public interface GeneralServer {
	public boolean start();
	public boolean stop();
	public void bind(String hostName, long port);
}
