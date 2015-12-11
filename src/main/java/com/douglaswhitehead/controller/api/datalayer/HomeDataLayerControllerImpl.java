package com.douglaswhitehead.controller.api.datalayer;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douglaswhitehead.controller.AbstractController;
import com.douglaswhitehead.datalayer.HomeDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.model.digitaldata.DigitalData;

@RestController
@RequestMapping("/digitaldata")
public class HomeDataLayerControllerImpl extends AbstractController implements HomeDataLayerController {
	
	@Autowired
	private HomeDataLayer dataLayer;

	@Override
	@RequestMapping("/home")
	public String home(HttpServletRequest request, Device device, HttpServletResponse response) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		/*
		String digitalData = digitalDataAdapter.adapt(dataLayer.index(request, response, device, cart, user));
		
		model.addAttribute("ensManAccountId", properties.getAccountId());
		model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated", auth);
		model.addAttribute("cartId", cartId);
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("digitalData", digitalData);
		*/
		
		//DigitalData digitalData = dataLayer.home(request, response, device, cart, user);
		String digitalData = digitalDataAdapter.adapt(dataLayer.home(request, response, device, cart, user));
		
		return digitalData;
	}

}
