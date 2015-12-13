package com.douglaswhitehead.controller.view;

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

import com.douglaswhitehead.configuration.EnsightenManageConfigProperties;
import com.douglaswhitehead.controller.data.AbstractDataController;
import com.douglaswhitehead.datalayer.LoginDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;

@Controller
@RequestMapping("/login")
public class LoginPageControllerImpl extends AbstractDataController implements LoginPageController {
	
	@Autowired
	protected EnsightenManageConfigProperties properties;
	
	@Autowired
	private LoginDataLayer dataLayer;
	
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) final String error, 
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
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.login(error, request, response, device, cart, user));
		
		model.addAttribute("ensManAccountId", properties.getAccountId());
		model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("digitalData",digitalData);
		
		return "login";
	}

}