package com.douglaswhitehead.controller.api;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

import com.douglaswhitehead.model.ShoppingCart;

public interface ShoppingCartApiController {
	
	public ShoppingCart get(UUID id, HttpServletRequest request, Device device, HttpServletResponse response);
	
	public ShoppingCart addToCart(UUID id, long productId, HttpServletRequest request, Device device, HttpServletResponse response);
	
	public ShoppingCart removeFromCart(UUID id, long productId, HttpServletRequest request, Device device, HttpServletResponse response);

}