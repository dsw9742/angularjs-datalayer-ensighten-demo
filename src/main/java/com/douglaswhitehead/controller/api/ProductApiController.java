package com.douglaswhitehead.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

import com.douglaswhitehead.model.Product;

public interface ProductApiController {
	
	public List<Product> list(HttpServletRequest request, Device device, HttpServletResponse response);

	public List<Product> listByCategory(String category, HttpServletRequest request, Device device, HttpServletResponse response);
	
	public Product get(long id, HttpServletRequest request, Device device, HttpServletResponse response);

}