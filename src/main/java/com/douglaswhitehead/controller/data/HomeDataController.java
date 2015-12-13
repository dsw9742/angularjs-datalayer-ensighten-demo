package com.douglaswhitehead.controller.data;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

public interface HomeDataController {
	
	public Map<String, Object> home(HttpServletRequest request, Device device, HttpServletResponse response);

}