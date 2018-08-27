package com.capgemini.jstk.transactionregistration.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.dao.CustomerRepository;
import com.capgemini.jstk.transactionregistration.dao.ProductRepository;
import com.capgemini.jstk.transactionregistration.dao.TransactionRepository;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchCustomerInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchProductInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchTransactionInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NotTrustedCustomerException;
import com.capgemini.jstk.transactionregistration.exceptions.TooHighProductWeightException;
import com.capgemini.jstk.transactionregistration.exceptions.TooMuchExpensiveProductsException;
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
		transactionEntity.setCustomer(customerRepository.findOne(transaction.getCustomerId()));

		checkIfCustomerExists(transaction.getCustomerId());
		checkIfProductsExists(transaction.getProductIds());
		checkTotalPriceLowerThan(5000.0, transaction.getProductIds(), transaction.getCustomerId());
		checkAmountOfProductsAboveSpecifiedPrice(7000.0, 2, transaction.getProductIds());
		
		List<TransactionTO> addedTransactionsList = new ArrayList<>();
		double weightCounter = 0.0;
		for (Long key : transaction.getProductIds()) {	
			checkProductWeightIsNotAbove25Kg(key);
			weightCounter += productRepository.findOne(key).getWeight();
			if (weightCounter > 25) {
				addedTransactionsList.add(TransactionMapper.toTransactionTO(transactionRepository.save(transactionEntity)));
				customerRepository.findOne(transaction.getCustomerId()).getTransactions().add(transactionEntity);
				transactionEntity.getProducts().clear();
				transactionEntity.getProducts().add(productRepository.findOne(key));
				weightCounter = productRepository.findOne(key).getWeight();
			} else {
				transactionEntity.getProducts().add(productRepository.findOne(key));
			}
		}
		addedTransactionsList.add(TransactionMapper.toTransactionTO(transactionRepository.save(transactionEntity)));
		customerRepository.findOne(transaction.getCustomerId()).getTransactions().add(transactionEntity);
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
	
	@Override
	public double sumOfCustomerTransactions(Long customerId) {
		//List<TransactionEntity> customerTransactionList = transactionRepository.findByCustomerId(customerId);
		//double sum = 0.0;
		//for (TransactionEntity transaction : customerTransactionList) {
		//	for (ProductEntity product : transaction.getProducts()) {
		//		sum += product.getUnitPrice();
		//	}
		//}
		//return sum;
		return transactionRepository.sumOfCustomerTransactions(customerId);
	}
	
	private void checkIfCustomerExists(Long customerId) {
		if (!customerRepository.exists(customerId)) {
			throw new NoSuchCustomerInDatabaseException("ID not found!");
		}
	}
	
	private void checkIfProductsExists(Collection<Long> productIds) {
		for(Long key : productIds){
			if(!productRepository.exists(key)){
				throw new NoSuchProductInDatabaseException("ID not found!");
			}
		}
	}
	
	private void checkProductWeightIsNotAbove25Kg(Long productId) {
		if(productRepository.findOne(productId).getWeight() >= 25){
			throw new TooHighProductWeightException();
		}
	}

	private void checkAmountOfProductsAboveSpecifiedPrice(double price, int maxAmount, Collection<Long> productIds) {
		Map<Long, Integer> productMapWithAmount = new HashMap<>();
		for (Long key : productIds) {
			System.out.println("Checking: " + productRepository.findOne(key).getName());
			if (productRepository.findOne(key).getUnitPrice() > price) {
				int newValue = (productMapWithAmount.containsKey(key)) ? productMapWithAmount.get(key) + 1 : 1;
				productMapWithAmount.put(key, newValue);
				System.out.println("\n\n\n\n IM HERE \n\n\n\n");

			}
		}
		for (Entry<Long, Integer> entry : productMapWithAmount.entrySet()) {
			if (entry.getValue().intValue() > maxAmount) {
				throw new TooMuchExpensiveProductsException(price);
			}
		}
	}

	private void checkTotalPriceLowerThan(double maxSum, Collection<Long> productIds, Long customerId) {
		double counter = 0.0;
		for (Long key : productIds) {
			counter += productRepository.findOne(key).getUnitPrice();
		}
		if (counter > maxSum) {
			Set<TransactionEntity> trasactionList = customerRepository.findOne(customerId).getTransactions();
			trasactionList = trasactionList.stream().filter(x -> x.getStatus() == TransactionStatus.REALISED).collect(Collectors.toSet());
			if (trasactionList.size() < 3) {
				throw new NotTrustedCustomerException();
			}
		}
	}
}
