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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.douglaswhitehead.controller.api.data.AbstractDataController;
import com.douglaswhitehead.datalayer.ErrorDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;

@Controller
public class ErrorPartialControllerImpl extends AbstractDataController implements ErrorPartialController {

	@Autowired
	private ErrorDataLayer dataLayer;
	
	@Override
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error(@RequestParam(value = "error", required = false) final String error, 
			final HttpServletRequest request, final Device device, final HttpServletResponse response, 
			final Model model) {
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
		String digitalData = digitalDataAdapter.adapt(dataLayer.error(HtmlUtils.htmlEscape(error), String.valueOf(response.getStatus()), request, response, device, model, cart, user));
		
		//model.addAttribute("ensManAccountId", properties.getAccountId());
		//model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated", auth);
		model.addAttribute("cartId", cartId);
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("error", HtmlUtils.htmlEscape(error));
		model.addAttribute("status", response.getStatus());
		model.addAttribute("digitalData", digitalData);
		
		return "error";
	}
}
