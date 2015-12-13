package com.douglaswhitehead.controller.data;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

public interface ProductsDataController {

	public Map<String, Object> list(HttpServletRequest request, Device device, HttpServletResponse response);
	
	public Map<String, Object> listByCategory(String category, HttpServletRequest request, Device device, HttpServletResponse response);

	public Map<String, Object> get(long id, HttpServletRequest request, Device device, HttpServletResponse response);
	
}