package com.creative.context;

import org.json.JSONObject;

public class JsonData implements IData{
  private JSONObject data;
  private String origin;
  public JsonData(String data){
    this.data = new JSONObject(data);
    this.origin = data;
  }
  public boolean setData(String data){
    try{
      this.data = new JSONObject(data);
      this.origin = data;
      return true;
    }catch(Exception e){
      return false;
    }
  }
  public String get(String key){return data.getString(key);}
  public String toString(){
    return origin;
  }
}
