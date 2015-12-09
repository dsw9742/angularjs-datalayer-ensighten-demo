package com.douglaswhitehead.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.douglaswhitehead.model.OrderForm;

public interface OrderPartialController {

	String checkout(OrderForm orderForm, HttpServletRequest request, Device device, 
			HttpServletResponse response, Model model);

	String complete(OrderForm orderForm, HttpServletRequest request, Device device, 
			HttpServletResponse response, Model model);

}
