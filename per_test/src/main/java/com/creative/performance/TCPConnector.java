/**
 * 
 */
package com.creative.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;


/**
 * @author ngocvd
 *
 */
public class TCPConnector {

  private Socket socket;
  private String host;
  private int port;
  /**
   * Setup TCP server properties
   * @param address
   * @param port
   */
  public void setUp(String host, int port) {
    InetAddress address;
    this.host = host;
    this.port = port;
    try {
      address = InetAddress.getByName(host);
      socket = new Socket(address, port);
      socket.close();
    } catch (UnknownHostException e) {
    } catch (IOException e) {
    }
  }


  public String sendMessage(String message) {
    try
    {
      if(socket == null) return "";
      if(!message.endsWith("\n")) message += "\n";
      //Send the message to the server
      if(socket.isClosed()) {
        InetAddress address = InetAddress.getByName(host);
        socket = new Socket(address, port);
      } 
      OutputStream os = socket.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);

      bw.write(message);
      bw.flush();

      //Get the return message from the server
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String result = br.readLine();
      
      return result;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      //Closing the socket
      try
      {
        socket.close();
      }
      catch(Exception e)
      {
      }
    }
    return "";
  }

}
