package com.creative.dns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorEvent;
import com.creative.disruptor.OnePublisherWorkerPool;
import com.lmax.disruptor.WorkHandler;


public class TCPServer {
  final static Logger logger = Logger.getLogger(TCPServer.class);
  private static OnePublisherWorkerPool pool;
  private static void setConfig(String[] args){
    
    ArrayList<WorkHandler<DisruptorEvent>> list = new ArrayList<>();
    list.add(new DNSHandler());
    pool = new OnePublisherWorkerPool(new DNSHandler());
  }


  public static void main(String[] args) {
    setConfig(args);
    try {
      @SuppressWarnings("resource")
      ServerSocket listenSocket = new ServerSocket(10002);
      logger.info("Start server at port: 10002" );
      while(true){
        Socket clientSocket = listenSocket.accept();
        try {
          BufferedReader inBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          String message;
          if(clientSocket.isClosed() || inBuffer == null) return;
          message = inBuffer.readLine();
          if(message != null){
            pool.push(new Context(clientSocket,message));
          }
        }catch (IOException | JSONException e) {
          logger.debug(e);
          try {
            clientSocket.close();
          } catch (IOException ex) {
            logger.debug(ex);
          }
        } 
      }
    }catch(Exception e){}
    finally{}
  }
}
