package com.capgemini.jstk.transactionregistration.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.dao.ProductRepository;
import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchProductInDatabaseException;
import com.capgemini.jstk.transactionregistration.mappers.ProductMapper;
import com.capgemini.jstk.transactionregistration.service.ProductService;
import com.capgemini.jstk.transactionregistration.types.ProductTO;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Transactional(readOnly = false)
	@Override
	public ProductTO saveProduct(ProductTO product) {
		return ProductMapper.toProductTO(productRepository.save(ProductMapper.toProductEntity(product)));
	}

	@Transactional(readOnly = false)
	@Override
	public ProductTO updateProduct(ProductTO product) {
		if(!productRepository.exists(product.getId())){
			throw new NoSuchProductInDatabaseException("ID not found!");
		}
		ProductEntity productEntity = productRepository.findOne(product.getId());
		productEntity.setName(product.getName());
		productEntity.setUnitPrice(product.getUnitPrice());
		productEntity.setMarginPercent(product.getMarginPercent());
		productEntity.setWeight(product.getWeight());
		productRepository.save(productEntity);
		return ProductMapper.toProductTO(productEntity);
	}

	@Transactional(readOnly = false)
	@Override
	public ProductTO deleteProduct(ProductTO product) {
		if(!productRepository.exists(product.getId())){
			throw new NoSuchProductInDatabaseException("ID not found!");
		}
		productRepository.delete(product.getId());
		return product;
	}

	@Override
	public boolean contains(Long id) {
		return productRepository.exists(id);
	}

	@Override
	public int size() {
		if (productRepository.findAll() instanceof Collection) {
		    return ((Collection<?>) productRepository.findAll()).size();
		}
		return -1;
	}

	@Override
	public ProductTO findProductById(Long id) {
		if(!productRepository.exists(id)){
			throw new NoSuchProductInDatabaseException("ID not found!");
		}
		return ProductMapper.toProductTO(productRepository.findOne(id));
	}
}
