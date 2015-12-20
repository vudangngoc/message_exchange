package com.creative.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.creative.GlobalConfig;
import org.apache.log4j.Logger;

public class TCPServer {
  final static Logger logger = Logger.getLogger(TCPServer.class);

  private static void setConfig(String[] args){
    if(args.length > 0 && isInteger(args[0])) {
      GlobalConfig.setConfig(GlobalConfig.PORT,args[0]);
    } else{
      GlobalConfig.setConfig(GlobalConfig.PORT,"10001");
    }
    if(args.length > 1 && isInteger(args[1])) {
      GlobalConfig.setConfig(GlobalConfig.WORKER_LIFE_TIME,args[1]);
    } else{
      GlobalConfig.setConfig(GlobalConfig.WORKER_LIFE_TIME,"10000");
    }
    if(args.length > 2 && isInteger(args[2])) {
      GlobalConfig.setConfig(GlobalConfig.RING_BUFFER_SIZE,args[2]);
    } else{
      GlobalConfig.setConfig(GlobalConfig.RING_BUFFER_SIZE,"512");
    }
  }


  public static void main(String[] args) {
    setConfig(args);
    try {
      @SuppressWarnings("resource")
      ServerSocket listenSocket = new ServerSocket(Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.PORT)));
      logger.info("Start server at port:" + GlobalConfig.getConfig(GlobalConfig.PORT));
      while(true){
        Socket clientSocket = listenSocket.accept();
        ClientHandler handler = new ClientHandler(clientSocket);
        handler.run();
      }
    }catch(Exception e){}
    finally{}
  }
  public static boolean isInteger(String s) {
    //http://stackoverflow.com/a/5439547/1402425
    try {
      Integer.parseInt(s);
    } catch(NumberFormatException | NullPointerException e) {
      return false;
    }
    // only got here if we didn't return false
    return true;
  }

}
