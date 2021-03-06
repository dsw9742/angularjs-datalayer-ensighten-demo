package com.douglaswhitehead.datalayer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.model.digitaldata.DigitalData;

public interface ErrorDataLayer {

	public DigitalData error(String error, String status, HttpServletRequest request, HttpServletResponse response, Device device, ShoppingCart cart, User user);
	
}
