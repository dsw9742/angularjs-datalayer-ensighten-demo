package com.douglaswhitehead.controller;

import java.util.List;
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

import com.douglaswhitehead.datalayer.ProductsDataLayer;
import com.douglaswhitehead.model.Product;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductPartialControllerImpl extends AbstractController implements ProductPartialController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductsDataLayer dataLayer;

	@Override
	@RequestMapping(method=RequestMethod.GET)
	public String list(final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		List<Product> products = productService.list();
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		String digitalData = digitalDataAdapter.adapt(dataLayer.list(products, request, response, device, model, cart, user));

		model.addAttribute("ensManAccountId", properties.getAccountId());
		model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated",auth);
		model.addAttribute("cartId", cartId);
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("products", products);
		model.addAttribute("digitalData", digitalData);
		
		return "products/list";
	}

	@Override
	@RequestMapping(value="/category/{category}", method=RequestMethod.GET)
	public String listByCategory(@PathVariable("category") final String category, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		List<Product> products = productService.listByCategory(category);
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		String digitalData = digitalDataAdapter.adapt(dataLayer.listByCategory(category, products, request, response, device, model, cart, user));

		model.addAttribute("ensManAccountId", properties.getAccountId());
		model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated",auth);
		model.addAttribute("cartId", cartId);
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("products", products);
		model.addAttribute("digitalData", digitalData);

		return "products/listByCategory";
	}

	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String get(@PathVariable("id") final long id, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			cartId = setNewCartIdCookie(request, response);
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		Product product = productService.get(id);
		if (product == null) {
			return "redirect:/error?error="+"No such product.";
		}
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		String digitalData = digitalDataAdapter.adapt(dataLayer.get(product, request, response, device, model, cart, user));
		
		model.addAttribute("ensManAccountId", properties.getAccountId());
		model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated",auth);
		model.addAttribute("cartId", cartId);
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("product", product);
		model.addAttribute("digitalData", digitalData);
		
		return "products/view";
	}

}
