package com.creative.front_server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creative.business.StateBusiness;
import com.creative.business.TimerBusiness;
import com.creative.connector.back_server.Connector;
import com.creative.service.StateService;
import com.creative.service.TimerCommand;

/**
 * Handles requests for the application home page.
 */
@Controller
public class TimerController {

  private static final Logger logger = LoggerFactory.getLogger(TimerController.class);
  DateFormat formatter = new SimpleDateFormat(TimerCommand.TIME_FORMAT);
  TimerBusiness business = new TimerBusiness();

  @RequestMapping(value = "/timer", method = RequestMethod.GET)
  public String state(Locale locale, Model model) {
  	
    TimerModel sm = new TimerModel();
    sm.setDeviceList(business.getDevices());
    sm.setTime(formatter.format(new Date()));
    model.addAttribute("timerModel", sm); 
    model.addAttribute("size", business.getNumberTimers());
    return "timer";
  }
  
  @RequestMapping(value = "/timer", method = RequestMethod.POST)
  public String state(@ModelAttribute TimerModel timerModel,Model model,@RequestParam String action) {
  	switch (action) {
		case "Create":
				business.createTimer("WEB_UI", timerModel.getDeviceId(), timerModel.getRepeatType(), timerModel.getTime(), timerModel.getState());
			break;
		case "Modify":
				business.updateTimer(timerModel.getTimerId(), timerModel.getRepeatType(), timerModel.getTime(), timerModel.getState());
			break;
		case "Delete":
			business.deleteTimer(timerModel.getTimerId());
			break;
		default:
			break;
		}
    TimerModel sm = new TimerModel();
    sm.setDeviceList(business.getDevices());
    sm.setTime(formatter.format(new Date()));
    model.addAttribute("timerModel", sm); 
    model.addAttribute("size", business.getNumberTimers());
    return "timer";
  }
  


}
