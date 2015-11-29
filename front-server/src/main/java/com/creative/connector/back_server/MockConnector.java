package com.creative.connector.back_server;

import java.util.ArrayList;
import java.util.List;

import com.creative.context.DataObjectFactory;
import com.creative.context.IData;
import com.creative.service.StateService;

public class MockConnector implements Connector {

  @Override
  public String sendMessage(String message) {
    IData data = DataObjectFactory.createDataObject();
    data.set(StateService.COMMAND, "STATE_SET");
    data.set(StateService.FROM, "X");
    data.set(StateService.TO, "X@Y");
    data.set(StateService.DATA, "OFF");
    IData data1 = DataObjectFactory.createDataObject();
    data1.set(StateService.COMMAND, "STATE_SET");
    data1.set(StateService.FROM, "X");
    data1.set(StateService.TO, "Z@Y");
    data1.set(StateService.DATA, "OFF");
    IData array = DataObjectFactory.createDataObject();
    List<String> temp = new ArrayList<String>();
    temp.add(data.toString());
    temp.add(data1.toString());
    array.setList("data", temp);
    return array.toString();
  }

}
