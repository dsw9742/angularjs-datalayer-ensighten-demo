package com.douglaswhitehead.repository;

import java.util.List;

import com.douglaswhitehead.model.Product;

public interface ProductRepository {
	
	public List<Product> list();

	public List<Product> listByCategory(String category);
	
	public Product get(long id);
	
}