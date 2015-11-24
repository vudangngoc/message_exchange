package com.creative.context;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonData implements IData{
  private JSONObject data;
  private String origin;

  public JsonData(){}

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
  public String get(String key){
    if(data != null && data.has(key)){
      try{
      return data.getString(key);
      }catch(JSONException e){
        return data.getJSONObject(key).toString();
      }
    }
    else return "";
  }
  public String toString(){
    return origin;
  }
}
