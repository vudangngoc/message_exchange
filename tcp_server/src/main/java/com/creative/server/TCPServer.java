package com.creative.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

import com.creative.GlobalConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
  static {
    ResourceBundle rb = ResourceBundle.getBundle("conf.configuration");
    JedisPoolConfig redisConfig = new JedisPoolConfig();
    redisConfig.setTestOnBorrow(false);
    redisConfig.setTestWhileIdle(true);
    redisConfig.setTestOnCreate(false);
    redisConfig.setTestOnReturn(false);
    redisConfig.setMaxTotal(128);
    redisPool = new JedisPool(redisConfig, rb.getString("redis.server"), Integer.parseInt(rb.getString("redis.port")),500,rb.getString("redis.password"));
  }
  public static JedisPool redisPool;

  public static void main(String[] args) {
    setConfig(args);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Closing database");
				redisPool.destroy();
			}
		});
		ClientHandler handler = new ClientHandler();
    try {
      @SuppressWarnings("resource")
      InetAddress addr = InetAddress.getByName("127.0.0.1");
      ServerSocket listenSocket = new ServerSocket(Integer.parseInt(GlobalConfig.getConfig(GlobalConfig.PORT)),50,addr);
      logger.info("Start server at port:" + GlobalConfig.getConfig(GlobalConfig.PORT));
      while(true){
        Socket clientSocket = listenSocket.accept();
        
        handler.run(clientSocket);
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
