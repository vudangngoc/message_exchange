package com.creative.dns;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.creative.crypto.StringXORer;
import com.creative.jdbc.Domain;
import com.creative.jdbc.DomainDAO;

public class WDT extends Thread {
	private HashMap<String, Domain> data;
	private final String key ="60637ee7-1f08-4733-b88f-3839801d3ffb";
	public WDT(HashMap<String, Domain> data){
		this.data = data;
		ResourceBundle rb = ResourceBundle.getBundle("conf.database");
		String url;
		String driver;
		String user;
		String password;
		if(rb != null){
			url = rb.getString("url");
			driver = rb.getString("driver");
			user = rb.getString("user");
			password = rb.getString("password");
		}else{
			url = "com.mysql.jdbc.Driver";
			driver = "jdbc:mysql://db.thietbithongminh.info:11501/message_exchange";
			user = "WFdZUEEB";
			password = "e1VCUnspDFRM";
		}
		StringXORer encrypt = new StringXORer();
		dao  = new DomainDAO(driver, url, encrypt.decode(user, key), encrypt.decode(password, key));
	}
	DomainDAO dao;
	@Override
	public void run(){
		while(true){
			DomainServer.isUpdating = true;
			List<Domain> update = dao.getUpdate();
			for(Domain d : update){
				if(d.getIsDelete()){
					data.remove(d.getName());
				}else{
					data.put(d.getName(), d);
				}
			}
			DomainServer.isUpdating = false; //Release lock
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}
}
