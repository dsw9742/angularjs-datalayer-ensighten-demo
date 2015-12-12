package com.douglaswhitehead.controller.api.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douglaswhitehead.datalayer.LoginDataLayer;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;

@RestController
@RequestMapping("/data")
public class LoginDataControllerImpl extends AbstractDataController implements LoginDataController {

	@Autowired
	private LoginDataLayer dataLayer;
	
	@Override
	@RequestMapping("/login")
	public Map<String, Object> login(@RequestParam(value = "error", required = false) final String error, final HttpServletRequest request, final Device device, final HttpServletResponse response) {
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
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.login(error, request, response, device, cart, user));
		
		//model.addAttribute("ensManAccountId", properties.getAccountId());
		//model.addAttribute("ensManPublishPath", properties.getPublishPath());
		map.put("isAuthenticated", auth);
		map.put("cartId", cartId);
		map.put("cartSize", calculateCartSize(cart));
		map.put("digitalData", digitalData);
		
		return map;
	}

}
