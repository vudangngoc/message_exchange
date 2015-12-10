package com.creative.performance;

import java.util.Random;

public class MainThread {

  public static void main(String[] args) {
    //Test status
    long round = 100000;
    int numberThread = 300;
    while(round > 0){
      if(TestThread.threadNumber > numberThread) continue;
      TestThread test = new TestThread("s1.thietbithongminh.info", 10001, getRandomMessage());
      test.run();
      round--;
    }

  }
  
  private static String getRandomMessage(){
    StringBuilder result = new StringBuilder();
    if(getRandom()%2 == 0){
      result.append("{COMMAND:STATE_SET;FROM:X;TO:"+getRandom()+"@demo;DATA:OFF}");
    }
    else{
      result.append("{COMMAND:STATE_GET;FROM:X;TO:"+getRandom()+"@demo;DATA:OFF}");
    }
    
    return result.toString();
  }
  private static Random rand = new Random();
  private static int getRandom(){
    
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    return rand.nextInt((100 - 0) + 1);
  }

}
