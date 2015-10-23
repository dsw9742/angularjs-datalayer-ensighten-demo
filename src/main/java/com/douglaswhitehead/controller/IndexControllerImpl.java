package com.douglaswhitehead.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douglaswhitehead.datalayer.IndexDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.service.ShoppingCartService;

@Controller
@RequestMapping("/")
public class IndexControllerImpl extends BaseControllerImpl implements IndexController {
	
	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private IndexDataLayer dataLayer;
	
	@RequestMapping(method=RequestMethod.GET)
	public String index(final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated(request.getUserPrincipal());
		String cartId;

		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		
		String digitalData = toString(dataLayer.index(request, device)); // TODO: push cart, user objects
		
		model.addAttribute("isAuthenticated", auth);
		model.addAttribute("cartId", cartId);
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("digitalData", digitalData);
		
		return "index";
	}

}