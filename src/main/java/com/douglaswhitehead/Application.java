package com.douglaswhitehead;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.douglaswhitehead.service.UserService;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    // cache, would likely be replaced with production-ready cachemanager in a prod env
    @Bean
    public CacheManager cacheManager() {
    	return new ConcurrentMapCacheManager("cart");
    }
    
    // db, would likely be replaced with production-ready db in a prod env
    @Bean
    public ServletRegistrationBean h2ServletRegistration() {
    	ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
    	registrationBean.addUrlMappings("/console/*");
    	return registrationBean;
    }
    
    // there is no transaction management in this example, would likely be added in a prod env
    
    // security, would likely be replaced with production-ready settings in a prod env
    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    	
    	@Autowired
    	private UserService userService;
    	
    	@Override
    	protected void configure(HttpSecurity http) throws Exception {
    		http
    			.authorizeRequests()
    				.antMatchers("/console/**")
    				.permitAll()
    				.and()
    			.authorizeRequests()
    				.anyRequest()
    				.anonymous()
    				.and()
    			.csrf()
    				.disable()
    			.headers()
    				.frameOptions()
    				.disable()
    			.formLogin()
    				//.loginPage("/login")
    				//.failureUrl("/login?error")
    				.permitAll()
    				.and()
    			.logout()
    				.logoutSuccessUrl("/")
    				.permitAll();
    	}
    	
    	@Override
    	public void configure(AuthenticationManagerBuilder auth) throws Exception {
    		auth
    			.userDetailsService(userService);
    		
    		/*
    		auth
    			.inMemoryAuthentication()
    				.withUser("admin")
    					.password("admin")
    					.roles("ADMIN", "USER")
    					.and()
    				.withUser("user")
    					.password("user")
    					.roles("USER");
    		*/
    	}
    	
    }
    
}
