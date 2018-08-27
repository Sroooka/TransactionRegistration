package com.capgemini.jstk.transactionregistration.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.ProductRepositoryCustom;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	
	@PersistenceContext
    protected EntityManager entityManager;

}
