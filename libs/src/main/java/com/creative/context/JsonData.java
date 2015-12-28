package com.creative.context;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonData implements IData{
  private JSONObject data;
  private String origin;

  public JsonData(){
    this.data = new JSONObject();
    this.origin = "";
  }

  public JsonData(String data){
  	try{	  
    this.data = new JSONObject(data);
  	}catch(JSONException e){
  		this.data = null;
  	}
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
  
  @Override
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
  
  @Override
  public boolean set(String key, String value){
    data.put(key, value);
    return true;
  }
  @Override
  public boolean setList(String key, List<String> value){
    JSONArray array = new JSONArray();
    for(String s : value)
      array.put(s);
    data.put(key, array);
    return true;
  }
  public List<String> getList(String key){
    // TODO improve
    ArrayList<String> result = new ArrayList<String>();
    if(data != null && data.has(key)){      
      JSONArray array = data.getJSONArray(key);
      for(int i = 0; i < array.length(); i++){
        try{
          result.add(array.getString(i));
        }catch(JSONException e){
          result.add(array.getJSONObject(i).toString());
        }
      }
    }
    return result;
  }

  @Override
  public String toString(){
    if(!"".equals(origin))
      return origin;
    else return data.toString();
  }
}
