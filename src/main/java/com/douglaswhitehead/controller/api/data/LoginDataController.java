package com.douglaswhitehead.controller.api.data;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

public interface LoginDataController {

	public Map<String, Object> login(String error, HttpServletRequest request, Device device, HttpServletResponse response);
	
}
