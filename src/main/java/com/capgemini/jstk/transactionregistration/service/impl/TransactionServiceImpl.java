package com.capgemini.jstk.transactionregistration.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.dao.CustomerRepository;
import com.capgemini.jstk.transactionregistration.dao.ProductRepository;
import com.capgemini.jstk.transactionregistration.dao.TransactionRepository;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchCustomerInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchProductInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchTransactionInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NotTrustedCustomerException;
import com.capgemini.jstk.transactionregistration.exceptions.TooHighProductWeightException;
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
	
	@Autowired
	ProductRepository productRepository;
	
	@Transactional(readOnly = false)
	@Override
	public List<TransactionTO> saveTransaction(TransactionTO transaction) {
		TransactionEntity transactionEntity = TransactionMapper.toTransactionEntity(transaction);
		checkIfCustomerExists(transaction.getCustomerId());
		transactionEntity.setCustomer(customerRepository.findOne(transaction.getCustomerId()));
		List<TransactionTO> addedTransactionsList = new ArrayList<>();
		int weightCounter = 0;
		for (Long key : transaction.getProductIds()) {
			checkIfProductExists(key);
			checkProductWeight(key);
			weightCounter += productRepository.findOne(key).getWeight();
			if (weightCounter > 25) {
				addedTransactionsList.add(TransactionMapper.toTransactionTO(transactionRepository.save(transactionEntity)));
				transactionEntity.getProducts().clear();
				transactionEntity.getProducts().add(productRepository.findOne(key));
			} else {
				transactionEntity.getProducts().add(productRepository.findOne(key));
			}
			addedTransactionsList.add(TransactionMapper.toTransactionTO(transactionRepository.save(transactionEntity)));
		}
		return addedTransactionsList;
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

	@Override
	public List<TransactionTO> findByProductsAmount(int amount) {
		return TransactionMapper.map2TOs(transactionRepository.findByProductsAmount(amount));
	}
	
	private void trustedCustomerValidator(Long customerId) {
		Set<TransactionEntity> trasactionList = customerRepository.findOne(customerId).getTransactions();
		trasactionList.stream().map(x -> x.getStatus() == TransactionStatus.REALISED).collect(Collectors.toList());
		if (trasactionList.size() >= 3) {
			throw new NotTrustedCustomerException();
		}
	}
	
	private void checkIfCustomerExists(Long customerId) {
		if (!customerRepository.exists(customerId)) {
			throw new NoSuchCustomerInDatabaseException("ID not found!");
		}
	}
	
	private void checkIfProductExists(Long productId) {
		if(!productRepository.exists(productId)){
			throw new NoSuchProductInDatabaseException("ID not found!");
		}
	}
	
	private void checkProductWeight(Long productId) {
		if(productRepository.findOne(productId).getWeight() >= 25){
			throw new TooHighProductWeightException();
		}
	}
}
