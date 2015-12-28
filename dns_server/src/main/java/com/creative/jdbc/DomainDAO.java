package com.creative.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class DomainDAO {
  final static Logger logger = Logger.getLogger(DomainDAO.class);
	private String password;
	private String username;
	private String url;
	private long lastUpdate = 0;
	private String query = "SELECT ip,name,isDelete FROM Domain WHERE lastUpdate > ";
	public DomainDAO(String driverName,String url, String username, String password){
    try {
      Class.forName(driverName.trim()).newInstance();
   } catch (Exception e) {
     logger.debug("Could not load JDBC Driver class " + driverName, e);
   }
    this.url = url;
    this.username = username;
    this.password = password;
	}
	
	public List<Domain> getUpdate(){
		List<Domain> result = new ArrayList<>();
		Connection conn = getConn();
		if(conn != null){
			try {
				
				CallableStatement statement = conn.prepareCall(getQuery());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					result.add(wrap(rs));
				}
			} catch (SQLException e) {
				System.out.println(e.toString());
			}
		}
		this.lastUpdate = System.currentTimeMillis();
		return result;
	}
	
	private String getQuery() {
		return query + lastUpdate;
	}

	public Connection getConn(){
    try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			return null;
		}
	}
	
	private Domain wrap(ResultSet row){
		Domain result = new Domain();
		try {
			result.setHost(row.getString("ip"));
			result.setName(row.getString("name"));
			result.setisDelete(row.getBoolean("isDelete"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
