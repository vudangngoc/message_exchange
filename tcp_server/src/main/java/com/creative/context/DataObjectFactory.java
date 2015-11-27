package com.creative.context;

/**
 * Deprecated class, will be remove when using IoC container 
 * @author vudangngoc@gmail.com
 *
 * Nov 27, 2015
 */
@Deprecated
public class DataObjectFactory {
  public static IData createDataObject(){
    return new JsonData();
  }
  public static IData createDataObject(String data){
    return new JsonData(data);
  }
}
