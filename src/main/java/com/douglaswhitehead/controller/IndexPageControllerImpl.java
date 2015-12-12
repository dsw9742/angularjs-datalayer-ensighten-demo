package com.douglaswhitehead.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douglaswhitehead.configuration.EnsightenManageConfigProperties;
import com.douglaswhitehead.controller.api.data.AbstractDataController;

@Controller
@RequestMapping("/")
public class IndexPageControllerImpl extends AbstractDataController implements IndexPageController {
	
	@Autowired
	protected EnsightenManageConfigProperties properties;
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		
		model.addAttribute("ensManAccountId", properties.getAccountId());
		model.addAttribute("ensManPublishPath", properties.getPublishPath());
		
		return "index";
	}

}