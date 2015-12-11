package com.douglaswhitehead.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;

import com.douglaswhitehead.datalayer.OrderDataLayer;
import com.douglaswhitehead.model.Address;
import com.douglaswhitehead.model.Order;
import com.douglaswhitehead.model.OrderForm;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.ShoppingCartItem;
import com.douglaswhitehead.model.User;

@Controller
@RequestMapping("/orders")
public class OrderPartialControllerImpl extends AbstractController implements OrderPartialController {
	
	@Autowired
	private OrderDataLayer dataLayer;

	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	@Override
	public String checkout(@ModelAttribute("orderForm") final OrderForm orderForm, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			String error = "";
			try {
				error = UriUtils.encodeQueryParam("No cartId cookie.", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "redirect:/error?error="+error;
		} else {
			Cookie cookie = getCartIdCookie(request);
			cartId = cookie.getValue();
		}
		
		ShoppingCart cart = cartService.get(UUID.fromString(cartId));
		
		if (cart.getCartItems() == null || cart.getCartItems().size() == 0) {
			String error = "";
			try {
				error = UriUtils.encodeQueryParam("No items in cart to checkout.", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "redirect:/error?error="+error;
		}
		
		User user = null;
		if (auth) {
			user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		String digitalData = digitalDataAdapter.adapt(dataLayer.checkout(request, response, device, model, cart, user));
		
		//model.addAttribute("ensManAccountId", properties.getAccountId());
		//model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated", auth);
		model.addAttribute("cartId", cart.getId().toString());
		model.addAttribute("cartSize", calculateCartSize(cart));
		model.addAttribute("digitalData", digitalData);
		
		return "orders/checkout";
	}
	
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	@Override
	public String complete(@ModelAttribute("orderForm") final OrderForm orderForm, final HttpServletRequest request, final Device device, final HttpServletResponse response, final Model model) {
		boolean auth = isAuthenticated();
		String cartId;

		if (!checkCartIdCookie(request)) {
			String error = "";
			try {
				error = UriUtils.encodeQueryParam("Order error. No cartId cookie.", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "redirect:/error?error="+error;
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
		
		String digitalData = digitalDataAdapter.adapt(dataLayer.complete(request, response, device, model, cart, order, user));
		
		//model.addAttribute("ensManAccountId", properties.getAccountId());
		//model.addAttribute("ensManPublishPath", properties.getPublishPath());
		model.addAttribute("isAuthenticated", auth);
		model.addAttribute("cartId", cart.getId().toString());
		model.addAttribute("cartSize", 0); // cart items is now 0
		model.addAttribute("order", order);
		model.addAttribute("digitalData", digitalData);
		
		return "orders/complete";
	}
	
}