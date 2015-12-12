package com.douglaswhitehead.controller.api.data;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.douglaswhitehead.datalayer.OrderDataLayer;
import com.douglaswhitehead.model.Address;
import com.douglaswhitehead.model.Order;
import com.douglaswhitehead.model.OrderForm;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.ShoppingCartItem;
import com.douglaswhitehead.model.User;

@RestController
@RequestMapping("/data/orders")
public class OrderDataControllerImpl extends AbstractDataController implements OrderDataController {

	@Autowired
	private OrderDataLayer dataLayer;
	
	@Override
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public Map<String, Object> checkout(final OrderForm orderForm, final HttpServletRequest request, final Device device,
			final HttpServletResponse response) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			String error = "No cartId cookie.";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", error);
			return  map;
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		
		if (cart.getCartItems() == null || cart.getCartItems().size() == 0) {
			String error = "No items in cart to checkout.";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", error);
			return  map;
		}
		
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.checkout(request, response, device, cart, user));
		
		map.put("isAuthenticated", auth);
		map.put("cartId", cart.getId().toString());
		map.put("cartSize", calculateCartSize(cart));
		map.put("digitalData", digitalData);
		
		return map;
	}

	@Override
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public Map<String, Object> complete(final OrderForm orderForm, final HttpServletRequest request, final Device device,
			final HttpServletResponse response) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			String error = "Order error. No cartId cookie.";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", error);
			return  map;
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		// in a prod env, we would validate shipping address info here
		// validateShippingAddress();
		Address shippingAddress = new Address(orderForm.getRecipientName(), orderForm.getLine1(), orderForm.getLine2(), 
								  orderForm.getCity(), orderForm.getStateProvince(), orderForm.getPostalCode(), 
								  orderForm.getCountry());
		
		// in a prod env, we would validate payment info here (or maybe in a separate form step)
		// validatePaymentInfo();
		
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		// simulates placing an order
		// in a prod env, this would be replaced with a real order service and back end
		try {
			// remove items from shopping cart, since they are being purchased
			List<ShoppingCartItem> itemsToRemove = new ArrayList<ShoppingCartItem>();
			if (cart != null) {
				for (ShoppingCartItem item: cart.getCartItems()) {
					for (int i=1;i<=item.getQuantity();i++) {
						itemsToRemove.add(item);	
					}
				}
			}
			for (ShoppingCartItem item: itemsToRemove) {
				cartService.removeFromCart(cart.getId(), item.getId());
			}
			
		    Thread.sleep(5000); // simulate long-running purchase transaction
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		// simulate order service response, which would return an order 
		Order order = new Order(UUID.randomUUID(), cart.getBasePrice(), cart.getVoucherCode(), cart.getVoucherDiscount(), 
					  cart.getCurrency(), cart.getTaxRate(), cart.getShipping(), cart.getShippingMethod(), 
					  cart.getPriceWithTax(), cart.getCartTotal(), cart.getCartItems(), shippingAddress);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.complete(request, response, device, cart, order, user));
		
		map.put("isAuthenticated", auth);
		map.put("cartId", cart.getId().toString());
		map.put("cartSize", 0); // cart items is now 0
		map.put("order", order);
		map.put("digitalData", digitalData);
		
		return map;
	}

}
