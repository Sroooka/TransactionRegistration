package com.capgemini.jstk.transactionregistration.service;

import com.capgemini.jstk.transactionregistration.types.ProductTO;

public interface ProductService {
	public ProductTO saveProduct(ProductTO customer);
	public ProductTO updateProduct(ProductTO customer);
	public ProductTO deleteProduct(ProductTO customer);
	public boolean contains(Long id);
	public int size();
	public ProductTO findProductById(Long id);
}
