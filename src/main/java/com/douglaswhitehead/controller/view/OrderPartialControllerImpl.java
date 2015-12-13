package com.douglaswhitehead.controller.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douglaswhitehead.controller.data.AbstractDataController;

@Controller
@RequestMapping("/partials/orders")
public class OrderPartialControllerImpl extends AbstractDataController implements OrderPartialController {
	
	@Override
	@RequestMapping(value="/{pageName}", method=RequestMethod.GET)
	public String get(@PathVariable("pageName") final String pageName, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		return "partials/orders/"+pageName;
	}
	
}