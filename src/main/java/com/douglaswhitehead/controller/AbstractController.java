package com.douglaswhitehead.controller;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.douglaswhitehead.adapter.DigitalDataToStringAdapter;
import com.douglaswhitehead.configuration.EnsightenManageConfigProperties;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.ShoppingCartItem;
import com.douglaswhitehead.service.ShoppingCartService;

public abstract class AbstractController {
	
	@Autowired
	protected EnsightenManageConfigProperties properties;
	
	private static final String CART_ID_COOKIE_NAME = "shoppingCartId";
	
	private static final int CART_ID_COOKIE_EXPIRY = 60 * 60 * 24 * 60; // 60 days
	
	private static final String CART_ID_COOKIE_PATH = "/";
	
	/**
	 * Digital Data Adapter - converts DigitalData object to String
	 */
	@Autowired
	protected DigitalDataToStringAdapter digitalDataAdapter;
	
	/**
	 * Shopping Cart Service
	 */
	@Autowired
	protected ShoppingCartService cartService;
	
	@PostConstruct
	public void init() {
		if(null == properties) {
			System.out.println("no properties");
		} else {
			System.out.println(properties.toString());
		}
	}
	
	/**
	 * Returns true if a principal is not anonymous, false otherwise.
	 * 
	 * @return boolean
	 */
	protected boolean isAuthenticated() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if (auth instanceof AnonymousAuthenticationToken) {
				return false;
			}	
			if (auth instanceof UsernamePasswordAuthenticationToken) {
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * Returns true if cart Id cookie is found, false otherwise.
	 * 
	 * @param request
	 * @return boolean
	 */
	protected boolean checkCartIdCookie(final HttpServletRequest request) {
		boolean flag = false;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(CART_ID_COOKIE_NAME)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * Sets a new cart Id cookie.
	 * 
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return String
	 */
	protected String setNewCartIdCookie(final HttpServletRequest request, final HttpServletResponse response) {
		Cookie cartIdCookie = new Cookie(CART_ID_COOKIE_NAME, UUID.randomUUID().toString());
		cartIdCookie.setMaxAge(CART_ID_COOKIE_EXPIRY);
		cartIdCookie.setPath(CART_ID_COOKIE_PATH);
		response.addCookie(cartIdCookie);
		
		return cartIdCookie.getValue();
	}
	
	/**
	 * Returns the cart Id cookie. 
	 * 
	 * @param HttpServletRequest request
	 * @return Cookie
	 */
	protected Cookie getCartIdCookie(final HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(CART_ID_COOKIE_NAME)) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	/**
	 * Sets cookie to the cart Id cookie expiry age.
	 * 
	 * @param Cookie cookie 
	 * @return int
	 */
	protected int setCartIdCookieExpiry(final Cookie cookie) {
		cookie.setMaxAge(CART_ID_COOKIE_EXPIRY);
		return CART_ID_COOKIE_EXPIRY;
	}
	
	/**
	 * Calculates size of the cart.
	 * 
	 * @param ShoppingCart cart
	 * @return int
	 */
	protected int calculateCartSize(final ShoppingCart cart) {
		int cartSize = 0;
		if (cart != null) {
			for (ShoppingCartItem item : cart.getCartItems()) {
				cartSize += item.getQuantity();
			}
		}
		return cartSize;
	}
	
}
