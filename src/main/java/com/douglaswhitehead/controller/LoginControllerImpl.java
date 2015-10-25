package com.douglaswhitehead.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginControllerImpl extends BaseControllerImpl implements LoginController {
	
	// TODO: add component/datalayer, model/digitalData

	@Override
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) final String error, 
			final HttpServletRequest request, final Device device, final HttpServletResponse response, 
			final Model model) {
		
		
		return "login";
	}

}