package com.douglaswhitehead.controller.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

public interface HomePartialController {
	
	public String get(HttpServletRequest request, Device device, HttpServletResponse response, Model model);

}