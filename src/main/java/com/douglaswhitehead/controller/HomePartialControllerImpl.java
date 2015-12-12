package com.douglaswhitehead.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/partials")
public class HomePartialControllerImpl implements HomePartialController {

	@Override
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		return "partials/home";
	}

}
