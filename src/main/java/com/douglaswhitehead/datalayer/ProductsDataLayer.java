package com.douglaswhitehead.datalayer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

import com.douglaswhitehead.model.Product;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.model.digitaldata.DigitalData;

public interface ProductsDataLayer {

	public DigitalData list(List<Product> products, HttpServletRequest request, HttpServletResponse response, 
			Device device, ShoppingCart cart, User user);
	
	public DigitalData listByCategory(String category, List<Product> products, HttpServletRequest request, 
			HttpServletResponse response, Device device, ShoppingCart cart, User user);
	
	public DigitalData get(Product product, HttpServletRequest request, HttpServletResponse response, 
			Device device, ShoppingCart cart, User user);
		
}