package com.douglaswhitehead.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douglaswhitehead.controller.api.data.AbstractDataController;
import com.douglaswhitehead.datalayer.ShoppingCartDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;

@Controller
@RequestMapping("/carts")
public class ShoppingCartPartialControllerImpl extends AbstractDataController implements ShoppingCartPartialController {
	
	@Autowired
	private ShoppingCartDataLayer dataLayer;
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public String get(@PathVariable("id") final UUID id, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated();
		if (!checkCartIdCookie(request)) {
			setNewCartIdCookie(request, response);
		}
		
		ShoppingCart cart = cartService.get(id);
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		String digitalData = digitalDataAdapter.adapt(dataLayer.get(request, response, device, model, cart, user));
		
		//model.addAttribute("ensManAccountId", properties.getAccountId());
		//model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated", auth);
		model.addAttribute("cartId", cart.getId().toString());
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("cart", cart);
		model.addAttribute("digitalData", digitalData);
		
		return "cart/view";
	}

	@RequestMapping(value = "/{id}/addToCart/{productId}", method=RequestMethod.GET)
	public String addToCart(@PathVariable("id") final UUID id, @PathVariable("productId") final long productId, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		String cartId;
		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		cartService.addToCart(UUID.fromString(cartId), productId);
	
		return "redirect:/carts/"+cartId;
	}
	
	@RequestMapping(value = "/{id}/removeFromCart/{productId}", method=RequestMethod.GET)
	public String removeFromCart(@PathVariable("id") final UUID id, @PathVariable("productId") final long productId, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		String cartId;
		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		cartService.removeFromCart(UUID.fromString(cartId), productId);
	
		return "redirect:/carts/"+cartId;
	}
	
}
