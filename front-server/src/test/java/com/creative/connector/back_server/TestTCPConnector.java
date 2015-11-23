package com.creative.connector.back_server;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

public class TestTCPConnector {
  @Test
  public void testSend(){
    //Given
    MockTCPServer server = new MockTCPServer(25000);
    Thread t = new Thread(server);
    t.start();
    TCPConnector connector = new TCPConnector();
    connector.setUp("127.0.0.1", 25000);
    //When
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String result = connector.sendMessage("Hi there!");
    //Then
    assertEquals("Hi there!",result);
    server.setRunning(false);
  }
  private static class MockTCPServer implements Runnable{
    private Socket socket;
    private int port;
    private boolean isRunning = true;
    public void setRunning(boolean state){
      this.isRunning = state;
    }
    public MockTCPServer(int port){
      this.port = port;
    }
    public void run(){
      try
      {
        if(port == 0)
          port = 25000;
        ServerSocket serverSocket = new ServerSocket(port);
        //Server is running always. This is done using this while(true) loop
        while(isRunning)
        {
          //Reading the message from the client
          socket = serverSocket.accept();
          InputStream is = socket.getInputStream();
          InputStreamReader isr = new InputStreamReader(is);
          BufferedReader br = new BufferedReader(isr);
          String message = br.readLine();
          //Sending the response back to the client.
          OutputStream os = socket.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os);
          BufferedWriter bw = new BufferedWriter(osw);
          bw.write(message + "\n");
          bw.flush();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        try
        {
          socket.close();
        }
        catch(Exception e){}
      }
    }
  }
}

