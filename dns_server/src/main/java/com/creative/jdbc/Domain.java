package com.creative.jdbc;

import java.util.Date;

public class Domain {
	private String name;
	private String host;
	private boolean isDelete;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public boolean getIsDelete() {
		return isDelete;
	}
	public void setisDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
}
