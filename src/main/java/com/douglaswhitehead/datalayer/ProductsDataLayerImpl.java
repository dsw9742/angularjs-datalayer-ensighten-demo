package com.douglaswhitehead.datalayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Component;

import com.douglaswhitehead.model.Product;
import com.douglaswhitehead.model.ShoppingCart;
import com.douglaswhitehead.model.User;
import com.douglaswhitehead.model.digitaldata.DigitalData;
import com.douglaswhitehead.model.digitaldata.DigitalDataImpl;
import com.douglaswhitehead.model.digitaldata.common.AttributesImpl;
import com.douglaswhitehead.model.digitaldata.common.CategoryImpl;
import com.douglaswhitehead.model.digitaldata.component.ComponentImpl;
import com.douglaswhitehead.model.digitaldata.event.EventImpl;
import com.douglaswhitehead.model.digitaldata.page.Page;
import com.douglaswhitehead.model.digitaldata.page.PageImpl;
import com.douglaswhitehead.model.digitaldata.page.PageInfoImpl;

@Component
public class ProductsDataLayerImpl extends AbstractDataLayer implements ProductsDataLayer {

	@Override
	public DigitalData list(final List<Product> products, final HttpServletRequest request, 
			final HttpServletResponse response, final Device device, final ShoppingCart cart, final User user) {
		return new DigitalDataImpl.Builder()
				.pageInstanceID("productsList-prod")
				.page(listPageAdapter(request, device))
				.product(productsAdapter.adapt(products.toArray(new Product[products.size()])))
				.cart(cartAdapter.adapt(cart))
				.transaction(orderAdapter.adapt(null))
				.event(new EventImpl[0])
				.component(new ComponentImpl[0])
				.user(usersAdapter.adapt(new User[]{user}))
				.privacy(privacyAdapter.defaultPrivacy()) // default privacy setting
				.version(VERSION)
			.build();
	}

	@Override
	public DigitalData listByCategory(final String category, final List<Product> products, final HttpServletRequest request, 
			final HttpServletResponse response, final Device device, final ShoppingCart cart, final User user) {
		return new DigitalDataImpl.Builder()
				.pageInstanceID("productsListByCategory-prod")
				.page(listByCategoryPageAdapter(request, device))
				.product(productsAdapter.adapt(products.toArray(new Product[products.size()])))
				.cart(cartAdapter.adapt(cart))
				.transaction(orderAdapter.adapt(null))
				.event(new EventImpl[0])
				.component(new ComponentImpl[0])
				.user(usersAdapter.adapt(new User[]{user}))
				.privacy(privacyAdapter.defaultPrivacy()) // default privacy setting
				.version(VERSION)
			.build();
	}

	@Override
	public DigitalData get(final Product product, final HttpServletRequest request, 
			final HttpServletResponse response, final Device device, final ShoppingCart cart, final User user) {
		return new DigitalDataImpl.Builder()
				.pageInstanceID("productsGet-prod")
				.page(getPageAdapter(product, request, device))
				.product(productsAdapter.adapt(new Product[]{product}))
				.cart(cartAdapter.adapt(cart))
				.transaction(orderAdapter.adapt(null))
				.event(new EventImpl[0])
				.component(new ComponentImpl[0])
				.user(usersAdapter.adapt(new User[]{user}))
				.privacy(privacyAdapter.defaultPrivacy()) // default privacy setting
				.version(VERSION)
			.build();
	}
	
	private Page listPageAdapter(final HttpServletRequest request, final Device device) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
		Date issueDate = null;
		Date effectiveDate = null;
		Date expiryDate = null;
		
		try {
			issueDate = simpleDateFormat.parse("12/28/2001"); // these are entirely made up for example purposes
			effectiveDate = simpleDateFormat.parse("12/29/2001"); // these are entirely made up for example purposes
			expiryDate = simpleDateFormat.parse("12/30/2099"); // these are entirely made up for example purposes
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new PageImpl.Builder()
			.pageInfo(new PageInfoImpl.Builder()
					.pageID("productsList")
					.pageName("Products")
					.destinationURL("") // does not work well server-side, plan to overwrite this value from client side
					.referringURL(request.getHeader("referer")) // does not work well server-side, plan to overwrite this value from client side
					.sysEnv(detector.detect(device))
					.variant("1")
					.version("1.0")
					.breadcrumbs(new String[]{}) // does not work well server-side, plan to overwrite this value from client side
					.author("Test McGee")
					.issueDate(issueDate)
					.effectiveDate(effectiveDate)
					.expiryDate(expiryDate)
					.language("en-US")
					.geoRegion("US")
					.industryCodes("") // empty industry codes for our demo retail app
					.publisher("Shirley J Tester")
				.build())
			.category(new CategoryImpl.Builder()
					.primaryCategory("products") // we use site section as the primary category for our demo retail app
				.build())
			.attributes(new AttributesImpl.Builder() // empty attributes object
				.build())
		.build();
	}
	
	private Page listByCategoryPageAdapter(final HttpServletRequest request, final Device device) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
		Date issueDate = null;
		Date effectiveDate = null;
		Date expiryDate = null;
		
		try {
			issueDate = simpleDateFormat.parse("12/28/2001"); // these are entirely made up for example purposes
			effectiveDate = simpleDateFormat.parse("12/29/2001"); // these are entirely made up for example purposes
			expiryDate = simpleDateFormat.parse("12/30/2099"); // these are entirely made up for example purposes
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new PageImpl.Builder()
			.pageInfo(new PageInfoImpl.Builder()
					.pageID("productsListByCategory")
					.pageName("Products for Category")
					.destinationURL("") // does not work well server-side, plan to overwrite this value from client side
					.referringURL(request.getHeader("referer")) // does not work well server-side, plan to overwrite this value from client side
					.sysEnv(detector.detect(device))
					.variant("1")
					.version("1.0")
					.breadcrumbs(new String[]{}) // does not work well server-side, plan to overwrite this value from client side
					.author("Test McGee")
					.issueDate(issueDate)
					.effectiveDate(effectiveDate)
					.expiryDate(expiryDate)
					.language("en-US")
					.geoRegion("US")
					.industryCodes("") // empty industry codes for our demo retail app
					.publisher("Shirley J Tester")
				.build())
			.category(new CategoryImpl.Builder()
					.primaryCategory("products") // we use site section as the primary category for our demo retail app
				.build())
			.attributes(new AttributesImpl.Builder() // empty attributes object
				.build())
		.build();
	}
	
	private Page getPageAdapter(final Product product, final HttpServletRequest request, final Device device) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
		Date issueDate = null;
		Date effectiveDate = null;
		Date expiryDate = null;
		
		try {
			issueDate = simpleDateFormat.parse("12/28/2001"); // these are entirely made up for example purposes
			effectiveDate = simpleDateFormat.parse("12/29/2001"); // these are entirely made up for example purposes
			expiryDate = simpleDateFormat.parse("12/30/2099"); // these are entirely made up for example purposes
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new PageImpl.Builder()
			.pageInfo(new PageInfoImpl.Builder()
					.pageID("productsGet")
					.pageName(product.getName()+"-"+product.getId())
					.destinationURL("") // does not work well server-side, plan to overwrite this value from client side
					.referringURL(request.getHeader("referer")) // does not work well server-side, plan to overwrite this value from client side
					.sysEnv(detector.detect(device))
					.variant("1")
					.version("1.0")
					.breadcrumbs(new String[]{}) // does not work well server-side, plan to overwrite this value from client side
					.author("Test McGee")
					.issueDate(issueDate)
					.effectiveDate(effectiveDate)
					.expiryDate(expiryDate)
					.language("en-US")
					.geoRegion("US")
					.industryCodes("") // empty industry codes for our demo retail app
					.publisher("Shirley J Tester")
				.build())
			.category(new CategoryImpl.Builder()
					.primaryCategory("products") // we use site section as the primary category for our demo retail app
				.build())
			.attributes(new AttributesImpl.Builder() // empty attributes object
				.build())
		.build();
	}

}