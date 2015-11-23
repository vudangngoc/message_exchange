/**
 * 
 */
package com.creative.connector.back_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ngocvd
 *
 */
public class TCPConnector implements Connector {

  private Socket socket;
  private static final Logger logger = LoggerFactory.getLogger(TCPConnector.class);
  /**
   * Setup TCP server properties
   * @param address
   * @param port
   */
  public void setUp(String host, int port) {
    InetAddress address;
    try {
      address = InetAddress.getByName(host);
      socket = new Socket(address, port);
    } catch (UnknownHostException e) {
      logger.debug("Cannot resolve host " + host);
    } catch (IOException e) {
      logger.debug("Cannot create socket");
    }
  }

  /* (non-Javadoc)
   * @see com.creative.connector.back_server.Connector#sendMessage(java.lang.String)
   */
  @Override
  public String sendMessage(String message) {
    try
    {
      if(socket == null) return "";
      if(!message.endsWith("\n")) message += "\n";
      //Send the message to the server
      OutputStream os = socket.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);

      bw.write(message);
      bw.flush();
      System.out.println("Message sent to the server");

      //Get the return message from the server
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String result = br.readLine();
      
      logger.debug("Message get from the server",result);
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
        logger.debug("Closing socket faile ",e);
      }
    }
    return "";
  }

}
