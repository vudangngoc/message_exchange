package com.creative.service;

import com.creative.context.Context;
import com.creative.context.IData;
import com.creative.context.JsonData;

public class MockContext extends Context {


  public MockContext(){

  }

  private String response;
  @Override
  public boolean setResponse(String response){
    this.response = response;
    return true;
  }

  public String getResponse(){
    return this.response;
  }

  private IData data;
  @Override
  public IData getRequest(){
    return this.data;
  }
  public boolean setRequest(String request){
    try {
      this.data = new JsonData(request);
    } catch(Exception e) {
      this.data = new JsonData("{}");
      return false;
    }
    return true;
  }
}
