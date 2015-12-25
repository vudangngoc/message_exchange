package com.creative.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.mysql.jdbc.Statement;

public class TestDomainDAO {

  @Test
  public void testGetUpdate(){
    //Given
    DomainDAO dao = new DomainDAO("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:mymemdb", "sa", "");
    Connection conn = dao.getConn();
    assertNotNull(conn);
    
    try {
      assertFalse(conn.isClosed());
      CallableStatement comm = conn.prepareCall("CREATE TABLE  Domain (name varchar(100) NOT NULL,name varchar(100) NOT NULL),isDelete boolean NOT NULL,lastUpdate bigInt NOT NULL);");
      comm.executeQuery();
      comm = conn.prepareCall("INSERT INTO Domain VALUES()");
      if(!conn.isClosed()) conn.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //When
    //Then
  }
}
