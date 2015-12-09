package com.douglaswhitehead.controller.api.datalayer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

import com.douglaswhitehead.model.digitaldata.DigitalData;

public interface HomeDataLayerController {
	
	public DigitalData home(HttpServletRequest request, Device device, HttpServletResponse response);

}