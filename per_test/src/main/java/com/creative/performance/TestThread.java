package com.creative.performance;

public class TestThread implements Runnable{
  public static int threadNumber = 0;
  private TCPConnector connector = new TCPConnector();
  private String message;
  public TestThread(String host, int port, String message){
    connector.setUp(host, port);
    this.message = message;
  }
  @Override
  public void run() {
    threadNumber++;
    long start = System.nanoTime();
    try{
      String result = connector.sendMessage(message);
    }catch(Exception e){
      threadNumber--;
      return;
    }
    System.out.println(System.nanoTime() - start); 
    threadNumber--;
  }
}
