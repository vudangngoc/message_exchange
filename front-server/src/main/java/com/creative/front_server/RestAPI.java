package com.creative.front_server;


import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.creative.business.TimerBusiness;

@RestController
public class RestAPI {
	TimerBusiness timerBusiness = new TimerBusiness();
	
  @RequestMapping("/getTimers")  
  public @ResponseBody String getTimers(@RequestParam(value="name") String name) {
      return timerBusiness.getTimers(name);
  }
  @RequestMapping("/getTimer")  
  public @ResponseBody String getTimer(@RequestParam(value="id") String id) {
      return timerBusiness.getTimer(id);
  }
}
