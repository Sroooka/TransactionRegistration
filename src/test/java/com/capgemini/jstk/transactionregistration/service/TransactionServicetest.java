package com.capgemini.jstk.transactionregistration.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchCustomerInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchProductInDatabaseException;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchTransactionInDatabaseException;
import com.capgemini.jstk.transactionregistration.types.CustomerTO;
import com.capgemini.jstk.transactionregistration.types.TransactionTO;
import com.capgemini.jstk.transactionregistration.types.CustomerTO.CustomerTOBuilder;
import com.capgemini.jstk.transactionregistration.types.ProductTO;
import com.capgemini.jstk.transactionregistration.types.ProductTO.ProductTOBuilder;
import com.capgemini.jstk.transactionregistration.types.TransactionTO.TransactionTOBuilder;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest (properties = "spring.profiles.active=mysql-test")
//@SpringBootTest (properties = "spring.profiles.active=hsql-test")
public class TransactionServicetest {

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ProductService productService;
	
	@Test
	public void shouldAddAndFindTransactionById(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		//when
		TransactionTO foundTransaction = transactionService.findTransactionById(savedTransaction.getId());
		
		// then
		assertEquals(foundTransaction.getId(), savedTransaction.getId());
		assertEquals(foundTransaction.getDate(), new GregorianCalendar(2018, 7, 15).getTime());
		assertEquals(foundTransaction.getStatus(), TransactionStatus.REALISED);
	}
	
	@Test (expected = NoSuchCustomerInDatabaseException.class)
	public void shouldReturnErrorWhenCreatingTransactionWithNonExistingCustomer(){
		//given when then
		transactionService.saveTransaction(getTransactionRealised(1L, new HashSet<Long>()));
	}
	
	@Test (expected = NoSuchProductInDatabaseException.class)
	public void shouldReturnErrorWhenCreatingTransactionWithNonExistingProducts(){
		//given 
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(1234L);

		//when then
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldThrowExceptionWhenFindingNonExistingTransaction(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		
		//when then
		transactionService.findTransactionById(savedTransaction.getId() + 123123L);
	}
	
	@Test
	public void shouldUpdateTransaction(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		TransactionTO updatedTransaction = getTransactionCanceled(123123L, new HashSet<Long>());
		updatedTransaction.setId(savedTransaction.getId());
		
		//when
		TransactionTO returnedUpdatedTransaction = transactionService.updateTransaction(updatedTransaction);
		
		// then
		assertEquals(returnedUpdatedTransaction.getId(), savedTransaction.getId());
		assertEquals(savedTransaction.getStatus(), TransactionStatus.REALISED);
		assertEquals(returnedUpdatedTransaction.getStatus(), TransactionStatus.CANCELED);
		assertEquals(returnedUpdatedTransaction.getDate(), new GregorianCalendar(2018, 7, 25).getTime());
		assertEquals(returnedUpdatedTransaction.getCustomerId(), savedCustomer.getId());
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldReturnErrorWhenUpdatingNonExistingTransaction(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		
		TransactionTO updatedTransaction = getTransactionRealised(savedCustomer.getId(), new HashSet<Long>());
		updatedTransaction.setId(savedTransaction.getId() + 123123L);
		
		//when then
		transactionService.updateTransaction(updatedTransaction);
	}
	
	@Test
	public void shouldDeleteTransaction(){
		//given		
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		
		//when
		transactionService.deleteTransaction(savedTransaction);
		
		// then
		assertFalse(transactionService.contains(savedTransaction.getId()));
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldReturnErrorWhenDeletingNonExistingTransaction(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		savedTransaction.setId(savedTransaction.getId() + 123123L);
		
		//when then
		transactionService.deleteTransaction(savedTransaction);
	}
	
	@Test
	public void shouldReturnCorrectSize(){
		//given when
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		int sizeBeforeAdding = transactionService.size();
		for(int i=0; i<10; i++){
			transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		}
		int sizeAfterAdding = transactionService.size();
		
		// then
		assertEquals(10, sizeAfterAdding - sizeBeforeAdding);
	}
	
	@Test
	public void shouldFindByMinimalProductAmount(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		for (int i = 0; i < 100; i++) {
			productIdList.add(savedProduct.getId());
		}
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), new HashSet<Long>()));
		
		//when
		List<TransactionTO> foundTransactions = transactionService.findByProductsAmount(99);
		
		//when
		assertEquals(foundTransactions.get(0).getId(), savedTransaction.getId());
	}
	
	private TransactionTO getTransactionRealised(Long customerId, Collection<Long> productIds){
		return new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 15).getTime())
				.withCustomerId(customerId)
				.withProductIds(productIds)
				.withTransactionStatus(TransactionStatus.REALISED)
				.build();
	}
	
	private TransactionTO getTransactionCanceled(Long customerId, Collection<Long> productIds){
		return new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 25).getTime())
				.withCustomerId(customerId)
				.withProductIds(productIds)
				.withTransactionStatus(TransactionStatus.CANCELED)
				.build();
	}
	
	private CustomerTO getCustomerKowalski(){
		return new CustomerTOBuilder()
				.withName("Jan")
				.withSurname("Kowalski")
				.withEmail("jan.kowalski@domain.com")
				.withPhone("123-123-123")
				.withAddress("Poznan, ul. Poznanska 500")
				.withBirth(new GregorianCalendar(1990, 12, 7).getTime())
				.build();
	}
	
	private CustomerTO getCustomerNowak(){
		return new CustomerTOBuilder()
				.withName("Krzysztof")
				.withSurname("Nowak")
				.withEmail("krzysztof.nowak@domain.com")
				.withPhone("321-321-321")
				.withAddress("Poznan, ul. Dluga 1")
				.withBirth(new GregorianCalendar(1990, 12, 7).getTime())
				.build();
	}
	
	private ProductTO getProduct(){
		return new ProductTOBuilder()
				.withName("Simple product")
				.withMarginPercent(0)
				.withUnitPrice(500)
				.withWeight(10.0)
				.build();
	}
}
