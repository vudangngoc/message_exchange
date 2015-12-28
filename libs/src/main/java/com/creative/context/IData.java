package com.creative.context;

import java.util.List;

public interface IData {
  public String get(String key);
  public boolean set(String key, String value);
  public boolean setList(String key, List<String> value);
  public List<String> getList(String key);
  public String toString();
  public boolean setData(String data);  
}
