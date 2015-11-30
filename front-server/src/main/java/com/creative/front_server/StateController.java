package com.creative.front_server;

import java.text.DateFormat;
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

import com.creative.business.StateBusiness;
import com.creative.connector.back_server.Connector;
import com.creative.service.StateService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class StateController {

  private static final Logger logger = LoggerFactory.getLogger(StateController.class);
  
  StateBusiness stateBusiness = new StateBusiness();

  @RequestMapping(value = "/state", method = RequestMethod.GET)
  public String state(Locale locale, Model model) {
    StateModel sm = new StateModel();
    sm.setDeviceList(stateBusiness.getAllDevice());
    model.addAttribute("stateModel", sm); 
    return "state";
  }
  @RequestMapping(value = "/state", method = RequestMethod.POST)
  public String state(@ModelAttribute StateModel state,Model model) {
    StateModel sm = new StateModel();
    sm.setDeviceList(stateBusiness.getAllDevice());
    model.addAttribute("stateModel",  sm);
    stateBusiness.setDeviceState(state.getDeviceId(), state.getState());
    return "state";
  }
}
