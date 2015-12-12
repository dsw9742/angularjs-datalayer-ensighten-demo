package com.douglaswhitehead.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

public interface OrderPartialController {

	public String get(String pageName, HttpServletRequest request, Device device, HttpServletResponse response, Model model);

}