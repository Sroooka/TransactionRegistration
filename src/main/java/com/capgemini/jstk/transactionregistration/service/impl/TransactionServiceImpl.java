package com.capgemini.jstk.transactionregistration.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.dao.CustomerRepository;
import com.capgemini.jstk.transactionregistration.dao.TransactionRepository;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchCustomerInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchTransactionInDatabaseException;
import com.capgemini.jstk.transactionregistration.mappers.TransactionMapper;
import com.capgemini.jstk.transactionregistration.service.TransactionService;
import com.capgemini.jstk.transactionregistration.types.TransactionTO;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Transactional(readOnly = false)
	@Override
	public TransactionTO saveTransaction(TransactionTO transaction) {
		return TransactionMapper.toTransactionTO(transactionRepository.save(TransactionMapper.toTransactionEntity(transaction)));
	}

	@Transactional(readOnly = false)
	@Override
	public TransactionTO updateTransaction(TransactionTO transaction) {
		if(!transactionRepository.exists(transaction.getId())){
			throw new NoSuchTransactionInDatabaseException("ID not found!");
		}
		TransactionEntity transactionEntity = transactionRepository.findOne(transaction.getId());
		transactionEntity.setDate(transaction.getDate());
		transactionEntity.setStatus(transaction.getStatus());
		transactionEntity.setProductsAmount(transaction.getProductsAmount());
		transactionRepository.save(transactionEntity);
		return TransactionMapper.toTransactionTO(transactionEntity);
	}

	@Transactional(readOnly = false)
	@Override
	public TransactionTO deleteTransaction(TransactionTO transaction) {
		if(!transactionRepository.exists(transaction.getId())){
			throw new NoSuchTransactionInDatabaseException("ID not found!");
		}
		transactionRepository.delete(transaction.getId());
		return transaction;
	}

	@Override
	public boolean contains(Long id) {
		return transactionRepository.exists(id);
	}

	@Override
	public int size() {
		if (transactionRepository.findAll() instanceof Collection) {
		    return ((Collection<?>) transactionRepository.findAll()).size();
		}
		return -1;
	}

	@Override
	public TransactionTO findTransactionById(Long id) {
		if(!transactionRepository.exists(id)){
			throw new NoSuchTransactionInDatabaseException("ID not found!");
		}
		return TransactionMapper.toTransactionTO(transactionRepository.findOne(id));
	}

	@Override
	public void setCustomerInTransaction(Long transactionId, Long customerId) {
		if(!transactionRepository.exists(transactionId)){
			throw new NoSuchTransactionInDatabaseException("ID not found!");
		}
		if(!customerRepository.exists(customerId)){
			throw new NoSuchCustomerInDatabaseException("ID not found!");
		}
		TransactionEntity transactionEntity = transactionRepository.findOne(transactionId);
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		transactionEntity.setCustomer(customerEntity);
		customerEntity.getTransactions().add(transactionEntity);
		transactionRepository.save(transactionEntity);
		customerRepository.save(customerEntity);
	}
}
