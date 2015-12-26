package com.creative.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.mysql.jdbc.Statement;

public class TestDomainDAO {

  @Test
  public void testGetUpdate(){
    //Given
    DomainDAO dao = new DomainDAO("com.mysql.jdbc.Driver", "jdbc:mysql://128.199.229.134:11501/message_exchange", "ngocvd", "MetaLLica");
    Connection conn = dao.getConn();
    assertNotNull(conn);
    
    try {
      List<Domain> result = dao.getUpdate();
      assertTrue(result.size() >= 1);
      if(!conn.isClosed()) conn.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //When
    //Then
  }
}
