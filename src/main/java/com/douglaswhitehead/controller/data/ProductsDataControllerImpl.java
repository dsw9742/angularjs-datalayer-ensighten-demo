package com.douglaswhitehead.controller.data;

import java.util.HashMap;
import java.util.List;
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

import com.douglaswhitehead.datalayer.ProductsDataLayer;
import com.douglaswhitehead.model.Product;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.service.ProductService;

@RestController
@RequestMapping("/data/products")
public class ProductsDataControllerImpl extends AbstractDataController implements ProductsDataController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductsDataLayer dataLayer;
	
	@Override
	@RequestMapping(method=RequestMethod.GET)
	public Map<String, Object> list(final HttpServletRequest request, final Device device, final HttpServletResponse response) {
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
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.list(products, request, response, device, cart, user));

		map.put("isAuthenticated",auth);
		map.put("cartId", cartId);
		map.put("cartSize", calculateCartSize(cart));
		map.put("products", products);
		map.put("digitalData", digitalData);
		
		return map;
	}

	@Override
	@RequestMapping(value="/category/{category}", method=RequestMethod.GET)
	public Map<String, Object> listByCategory(@PathVariable("category") final String category, final HttpServletRequest request, final Device device,
			final HttpServletResponse response) {
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
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.listByCategory(category, products, request, response, device, cart, user));

		map.put("isAuthenticated",auth);
		map.put("cartId", cartId);
		map.put("cartSize", calculateCartSize(cart));
		map.put("products", products);
		map.put("digitalData", digitalData);
		
		return map;
	}

	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Map<String, Object> get(@PathVariable("id") final long id, final HttpServletRequest request, final Device device, final HttpServletResponse response) {
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
			String error = "No such product.";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", error);
			return map;
		}
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.get(product, request, response, device, cart, user));
		
		map.put("isAuthenticated",auth);
		map.put("cartId", cartId);
		map.put("cartSize", calculateCartSize(cart));
		map.put("product", product);
		map.put("digitalData", digitalData);
		
		return map;
	}

}
