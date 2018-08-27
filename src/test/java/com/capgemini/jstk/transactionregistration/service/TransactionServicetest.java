package com.capgemini.jstk.transactionregistration.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
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
import com.capgemini.jstk.transactionregistration.exceptions.NotTrustedCustomerException;
import com.capgemini.jstk.transactionregistration.exceptions.TooMuchExpensiveProductsException;
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
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		//when
		TransactionTO foundTransaction = transactionService.findTransactionById(savedTransactions.get(0).getId());
		
		// then
		assertEquals(savedTransactions.size(), 1);
		assertEquals(foundTransaction.getId(), savedTransactions.get(0).getId());
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
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		//when then
		transactionService.findTransactionById(savedTransactions.get(0).getId() + 123123L);
	}
	
	@Test
	public void shouldUpdateTransaction(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		TransactionTO updatedTransaction = getTransactionCanceled(123123L, productIdList);
		updatedTransaction.setId(savedTransactions.get(0).getId());
		
		//when
		TransactionTO returnedUpdatedTransaction = transactionService.updateTransaction(updatedTransaction);
		
		// then
		assertEquals(returnedUpdatedTransaction.getId(), savedTransactions.get(0).getId());
		assertEquals(savedTransactions.get(0).getStatus(), TransactionStatus.REALISED);
		assertEquals(returnedUpdatedTransaction.getStatus(), TransactionStatus.CANCELED);
		assertEquals(returnedUpdatedTransaction.getDate(), new GregorianCalendar(2018, 7, 25).getTime());
		assertEquals(returnedUpdatedTransaction.getCustomerId(), savedCustomer.getId());
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldReturnErrorWhenUpdatingNonExistingTransaction(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		TransactionTO updatedTransaction = getTransactionRealised(savedCustomer.getId(), productIdList);
		updatedTransaction.setId(savedTransactions.get(0).getId() + 123123L);
		
		//when then
		transactionService.updateTransaction(updatedTransaction);
	}
	
	@Test
	public void shouldDeleteTransaction(){
		//given		
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		//when
		transactionService.deleteTransaction(savedTransactions.get(0));
		
		// then
		assertFalse(transactionService.contains(savedTransactions.get(0).getId()));
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldReturnErrorWhenDeletingNonExistingTransaction(){
		//given		
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		savedTransactions.get(0).setId(123123123L);
		//when then
		transactionService.deleteTransaction(savedTransactions.get(0));
	}
	
	@Test
	public void shouldReturnCorrectSize(){
		//given when
		ProductTO savedProduct = productService.saveProduct(getProduct());
		HashSet<Long> productIdList = new HashSet<>();
		productIdList.add(savedProduct.getId());
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		
		int sizeBeforeAdding = transactionService.size();
		for(int i=0; i<10; i++){
			transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		}
		int sizeAfterAdding = transactionService.size();
		
		// then
		assertEquals(10, sizeAfterAdding - sizeBeforeAdding);
	}
	
	@Test
	public void shouldFindByMinimalProductAmount(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getCheapProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			productIdList.add(savedProduct.getId());
		}
		List<TransactionTO> savedTransactions = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		//when
		List<TransactionTO> foundTransactions = transactionService.findByProductsAmount(99);
		
		//when
		//assertEquals(foundTransactions.size(), 123123);
		assertEquals(savedTransactions.size(), 1);
		assertEquals(savedTransactions.get(0).getProductsAmount(), 100);
		//assertEquals(foundTransactions.get(0).getId(), savedTransactions.get(0).getId());
	}
	
	@Test
	public void shouldAddProductsAbove5000ToCustomerWith3RealisedTransactions(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct.getId());
		}
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		ProductTO savedExpensiveProduct = productService.saveProduct(getExpensiveProduct());
		List<Long> expensiveProductIdList = new ArrayList<>();
		expensiveProductIdList.add(savedExpensiveProduct.getId());
		expensiveProductIdList.add(savedExpensiveProduct.getId());
		
		// when 
		List<TransactionTO> transactionList = transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), expensiveProductIdList));

		// then
		assertEquals(transactionList.size(), 1);
		assertEquals(transactionList.get(0).getProductsAmount(), 2);
	}
	
	@Test (expected = NotTrustedCustomerException.class)
	public void shouldReturnErrorWhenAddingProductsAbove5000ToCustomerWithout3RealisedTransactions(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			productIdList.add(savedProduct.getId());
		}
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		
		// when then
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
	}
	
	@Test (expected = TooMuchExpensiveProductsException.class)
	public void shouldReturnErrorWhenAdd3ProductsWithPriceBelow7000(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct.getId());
		}
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		
		ProductTO savedExpensiveProduct = productService.saveProduct(get10kProduct());
		List<Long> expensiveProductIdList = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			expensiveProductIdList.add(savedExpensiveProduct.getId());
		}
		
		// when then
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), expensiveProductIdList));
	}
	
	@Test
	public void shouldCountSumOfCustomerTransactionsPrice(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct.getId());
		}
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		
		// when 
		double sum = transactionService.sumOfCustomerTransactions(savedCustomer.getId());
		// then
		assertEquals(sum, 3 * 10 * 500, 0.01);
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
	
	private ProductTO getCheapProduct(){
		return new ProductTOBuilder()
				.withName("Simple product")
				.withMarginPercent(0)
				.withUnitPrice(1)
				.withWeight(0.1)
				.build();
	}
	
	private ProductTO getProduct(){
		return new ProductTOBuilder()
				.withName("Simple product")
				.withMarginPercent(0)
				.withUnitPrice(500)
				.withWeight(0.1)
				.build();
	}
	
	private ProductTO getExpensiveProduct(){
		return new ProductTOBuilder()
				.withName("Expensive product")
				.withMarginPercent(0)
				.withUnitPrice(4000)
				.withWeight(0.1)
				.build();
	}
	
	private ProductTO get10kProduct(){
		return new ProductTOBuilder()
				.withName("Expensive product")
				.withMarginPercent(0)
				.withUnitPrice(10000)
				.withWeight(500.0)
				.build();
	}
}
