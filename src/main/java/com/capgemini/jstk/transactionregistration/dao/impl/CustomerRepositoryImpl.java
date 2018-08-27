package com.capgemini.jstk.transactionregistration.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.CustomerRepositoryCustom;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {
	
	@PersistenceContext
    protected EntityManager entityManager;
	
}
