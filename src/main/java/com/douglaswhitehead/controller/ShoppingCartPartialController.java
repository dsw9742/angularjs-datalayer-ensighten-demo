package com.douglaswhitehead.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

public interface ShoppingCartPartialController {
	
	public String get(UUID id, HttpServletRequest request, Device device, HttpServletResponse response, Model model);
	
	public String addToCart(UUID id, long productId, HttpServletRequest request, Device device, HttpServletResponse response, Model model);
	
	public String removeFromCart(UUID id, long productId, HttpServletRequest request, Device device, HttpServletResponse response, Model model);

}