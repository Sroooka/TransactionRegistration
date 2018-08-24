package com.capgemini.jstk.transactionregistration.dao.impl;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.dao.ProductRepositoryCustom;
import com.capgemini.jstk.transactionregistration.domain.query.QCustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QProductEntity;
import com.capgemini.jstk.transactionregistration.domain.query.QTransactionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	
	@PersistenceContext
    protected EntityManager entityManager;
	
	private JPAQueryFactory queryFactory;
	private QCustomerEntity qCustomer;
	private QProductEntity qProduct;
	private QTransactionEntity qTransaction;
	
	@PostConstruct
	private void init(){
		queryFactory = new JPAQueryFactory(entityManager);
		qCustomer = QCustomerEntity.customerEntity;
		qProduct = QProductEntity.productEntity;
		qTransaction = QTransactionEntity.transactionEntity;
	}
}
