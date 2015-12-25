package com.creative.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import jdbchelper.JdbcException;

public class DomainDAO {
	private String password;
	private String username;
	private String url;
	private long lastUpdate;
	private String query = "SELECT host,name,isDelete FROM Domain WHERE lastUpdate > " + lastUpdate;
	public DomainDAO(String driverName,String url, String username, String password){
    try {
      Class.forName(driverName.trim()).newInstance();
   } catch (Exception e) {
      throw new JdbcException("Could not load JDBC Driver class " + driverName, e);
   }
    this.url = url;
    this.username = username;
    this.password = password;
	}
	
	public List<Domain> getUpdateSince(long lastUpdate){
		List<Domain> result = new ArrayList<>();
		Connection conn = getConn();
		if(conn != null){
			try {
				CallableStatement statement = conn.prepareCall(query);
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					result.add(wrap(rs));
				}
			} catch (SQLException e) {
			}
		}
		return result;
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
		
		return result;
	}
}
