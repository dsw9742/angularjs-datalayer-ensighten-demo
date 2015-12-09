package com.douglaswhitehead.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

public interface ProductPartialController {
	
	public String list(HttpServletRequest request, Device device, HttpServletResponse response, Model model);

	public String listByCategory(String category, HttpServletRequest request, Device device, HttpServletResponse response, Model model);
	
	public String get(long id, HttpServletRequest request, Device device, HttpServletResponse response, Model model);

}
