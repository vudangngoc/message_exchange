package com.creative.front_server;

import java.util.Map;

public class StateModel {
  Map<String,String> deviceList;

  public Map<String, String> getDeviceList() {
    return deviceList;
  }

  public void setDeviceList(Map<String, String> deviceList) {
    this.deviceList = deviceList;
  }
  
  String deviceId;
  String state;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
