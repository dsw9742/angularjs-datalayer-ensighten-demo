package com.douglaswhitehead.controller.data;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

import com.douglaswhitehead.model.OrderForm;

public interface OrderDataController {
	
	public Map<String, Object> checkout(OrderForm orderForm, HttpServletRequest request, Device device, HttpServletResponse response);

	public Map<String, Object> complete(OrderForm orderForm, HttpServletRequest request, Device device, HttpServletResponse response);
	
}