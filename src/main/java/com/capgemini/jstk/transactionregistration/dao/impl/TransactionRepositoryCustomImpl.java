package com.capgemini.jstk.transactionregistration.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.TransactionRepositoryCustom;

@Repository
public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {
	
	@PersistenceContext
    protected EntityManager entityManager;
	
}
