package com.creative.context;

import java.util.List;

public interface IData {
  public String get(String key);
  public List<String> getList(String key);
  public String toString();
}
