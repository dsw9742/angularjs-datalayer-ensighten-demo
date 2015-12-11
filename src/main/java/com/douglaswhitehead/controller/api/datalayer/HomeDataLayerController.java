package com.douglaswhitehead.controller.api.datalayer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

public interface HomeDataLayerController {
	
	public Map<String, Object> home(HttpServletRequest request, Device device, HttpServletResponse response);

}