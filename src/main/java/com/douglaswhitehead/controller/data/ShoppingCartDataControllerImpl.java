package com.douglaswhitehead.controller.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.douglaswhitehead.datalayer.ShoppingCartDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.service.ShoppingCartService;

@RestController
@RequestMapping("/data/carts")
public class ShoppingCartDataControllerImpl extends AbstractDataController implements ShoppingCartDataController {

	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private ShoppingCartDataLayer dataLayer;
	
	@Override
	@RequestMapping(value = "/{id}", method=RequestMethod.GET)
	public Map<String, Object> get(@PathVariable("id") final UUID id, final HttpServletRequest request, final Device device, final HttpServletResponse response) {
		boolean auth = isAuthenticated();
		if (!checkCartIdCookie(request)) {
			setNewCartIdCookie(request, response);
		}
		
		ShoppingCart cart = cartService.get(id);
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.get(request, response, device, cart, user));

		map.put("isAuthenticated", auth);
		map.put("cartId", cart.getId().toString());
		map.put("cartSize", calculateCartSize(cart));
		map.put("cart", cart);
		map.put("digitalData", digitalData);
		
		return map;
	}

	@Override
	@RequestMapping(value = "/{id}/addToCart/{productId}", method=RequestMethod.PUT)
	public ShoppingCart addToCart(@PathVariable("id") final UUID id, @PathVariable("productId") final long productId, final HttpServletRequest request, final Device device,
			final HttpServletResponse response) {
		String cartId;
		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		ShoppingCart cart = cartService.addToCart(UUID.fromString(cartId), productId);
		
		return cart;
	}

	@Override
	@RequestMapping(value = "/{id}/removeFromCart/{productId}", method=RequestMethod.PUT)
	public ShoppingCart removeFromCart(@PathVariable("id") final UUID id, @PathVariable("productId") final long productId, final HttpServletRequest request, final Device device,
			final HttpServletResponse response) {
		String cartId;
		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		ShoppingCart cart = cartService.removeFromCart(UUID.fromString(cartId), productId);
		
		return cart;
	}

}
