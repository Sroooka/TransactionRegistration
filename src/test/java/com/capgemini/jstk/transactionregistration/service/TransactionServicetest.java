package com.capgemini.jstk.transactionregistration.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.domain.TransactionSearchCriteria;
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
		transactionService.findByProductsAmount(99);
		
		//when
		assertEquals(savedTransactions.size(), 1);
		assertEquals(savedTransactions.get(0).getProductsAmount(), 100);
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
	
	@Test
	public void shouldCountSumOfCustomerTransactionsPriceWithSpecifiedStatus(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct.getId());
		}
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer.getId(), productIdList));
		transactionService.saveTransaction(getTransactionCanceled(savedCustomer.getId(), productIdList));
		
		// when 
		double sum = transactionService.sumOfCustomerTransactionsWithTransactionStatus(savedCustomer.getId(), TransactionStatus.REALISED);
		
		// then
		assertEquals(sum, 2 * 10 * 500, 0.01);
	}
	
	@Test
	public void shouldCountSumOfAllTransactionsPriceWithSpecifiedStatus(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct.getId());
		}
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionCanceled(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		transactionService.saveTransaction(getTransactionCanceled(savedCustomer2.getId(), productIdList));
		
		// when 
		double sum = transactionService.sumOfTransactionsWithTransactionStatus(TransactionStatus.REALISED);
		
		// then
		assertEquals(sum, 2 * 2 * 10 * 500, 0.01);
	}
	
	@Test
	public void shouldFindTenBestSellingProducts(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct3 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct4 = productService.saveProduct(getCheapProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			productIdList.add(savedProduct1.getId());
		}
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct2.getId());
		}
		productIdList.add(savedProduct3.getId());
		productIdList.add(savedProduct4.getId());
		
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		// when 
		List<ProductTO> rankingList = transactionService.findBestSellingProducts(10);
		
		// then
		assertEquals(rankingList.size(), 4);
		assertEquals(rankingList.get(0).getId(), savedProduct2.getId());
		assertEquals(rankingList.get(1).getId(), savedProduct1.getId());
		assertEquals(rankingList.get(2).getId(), savedProduct3.getId());
	}
	
	@Test
	public void shouldFindCustomersWhoSpendMostMoneyInSpecifiedTime(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		CustomerTO savedCustomer3 = customerService.saveCustomer(getCustomerNowak());
		CustomerTO savedCustomer4 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct = productService.saveProduct(getCheapProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			productIdList.add(savedProduct.getId());
		}
		
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer3.getId(), productIdList));
		
		// when 
		List<CustomerTO> rankingList = transactionService.findCustomersWhoSpentMostMoneyInSpecifiedTime(3,  new GregorianCalendar(2018, 7, 10).getTime(), new GregorianCalendar(2018, 7, 20).getTime());		
		
		// then
		assertEquals(rankingList.size(), 3);
		assertEquals(rankingList.get(0).getId(), savedCustomer1.getId());
		assertEquals(rankingList.get(1).getId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldReturnProfitFromPeriodTime(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		CustomerTO savedCustomer3 = customerService.saveCustomer(getCustomerNowak());
		CustomerTO savedCustomer4 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct = productService.saveProduct(getCheapProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct.getId());
		}
		
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer4.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer3.getId(), productIdList));
		
		// when 
		double profit = transactionService.profitFromPeriodTime(new GregorianCalendar(2018, 7, 10).getTime(), new GregorianCalendar(2018, 7, 20).getTime());		
		
		// then
		assertEquals(profit, 10 * 6 * savedProduct.getUnitPrice() * savedProduct.getMarginPercent() * 0.01, 0.01);
	}
	
	@Test
	public void shouldFindAllProductsPreparedForDeliveryWithNameAndAmount(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		productIdList.add(savedProduct2.getId());
		
		transactionService.saveTransaction(getTransactionPreparedForDelivery(savedCustomer1.getId(), productIdList));

		// when 
		Map<String, Long> list = transactionService.findProductsPreparedForDelivery();
		for(Map.Entry<String, Long> entry : list.entrySet()) {
		    System.out.println("key(string): " + entry.getKey());
		    System.out.println("value(amount): " + entry.getValue());
		}
		
		// then
		assertEquals(list.size(), 2);
		assertTrue(list.containsKey(savedProduct1.getName()));
		assertTrue(list.containsKey(savedProduct2.getName()));
		assertEquals(list.get(savedProduct1.getName()), new Long(10));
		assertEquals(list.get(savedProduct2.getName()), new Long(1));
	}
	
	@Test
	public void shouldFindByCriteriaCustomerName(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));

		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setCustomerName("Jan");
		criteria.setCustomerSurame("Kowalski");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);
		
		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer1.getId());
	}
	
	@Test
	public void shouldFindByCriteriaPeriodTime(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));

		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaProductId(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		productIdList.add(savedProduct2.getId());
		List<TransactionTO> savedTransaction1 = transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), new ArrayList<Long>()));

		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setProductId(savedProduct1.getId());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getId(), savedTransaction1.get(0).getId());
	}
	
	@Test
	public void shouldFindByCriteriaTotalTransactionCost(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		List<TransactionTO> savedTransaction2 = transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer2.getId(), productIdList));

		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getId(), savedTransaction2.get(0).getId());
	}
	
	@Test
	public void shouldFindByCriteriaCustomerNameAndDate(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setProductId(savedProduct2.getId());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaCustomerNameAndProductId(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaCustomerNameTotalCost(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		List<TransactionTO> savedTransaction2 = transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer2.getId(), productIdList));

		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getId(), savedTransaction2.get(0).getId());
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaPeriodTimeAndProductId(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setProductId(savedProduct2.getId());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaPeriodTimeAndTotalPrice(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaProductIdAndTotalPrice(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setProductId(savedProduct1.getId());
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaProductIdAndTotalPriceAndPeriodTime(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setProductId(savedProduct1.getId());
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaCustomerNameProcuctIdAndTotalPrice(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setProductId(savedProduct1.getId());
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaCustomerNamePeriodTimeAndTotalPrice(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByCriteriaCustomerNamePeriodTimeAndProductId(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setProductId(savedProduct1.getId());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldFindByAllCriteria(){
		//given
		CustomerTO savedCustomer1 = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO savedCustomer2 = customerService.saveCustomer(getCustomerNowak());
		ProductTO savedProduct1 = productService.saveProduct(getCheapProduct());
		ProductTO savedProduct2 = productService.saveProduct(getProduct());
		List<Long> productIdList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			productIdList.add(savedProduct1.getId());
		}
		transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer1.getId(), productIdList));
		productIdList.add(savedProduct2.getId());
		transactionService.saveTransaction(getTransactionRealised(savedCustomer2.getId(), productIdList));
		
		TransactionSearchCriteria criteria = new TransactionSearchCriteria();
		criteria.setFrom(new GregorianCalendar(2018, 7, 10).getTime());
		criteria.setTo(new GregorianCalendar(2018, 7, 20).getTime());
		criteria.setProductId(savedProduct1.getId());
		criteria.setCustomerName("Krzysztof");
		criteria.setCustomerSurame("Nowak");
		criteria.setTotalPrice(10 * savedProduct1.getUnitPrice() + savedProduct2.getUnitPrice());
		
		// when 
		List<TransactionTO> list = transactionService.findBySearchCriteria(criteria);

		// then
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getCustomerId(), savedCustomer2.getId());
	}
	
	@Test
	public void shouldCascadeWorkCorrectWhenDeletingCustomer(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		ProductTO savedProduct = productService.saveProduct(getCheapProduct());
		List<Long> productIdList = new ArrayList<>();
		productIdList.add(savedProduct.getId());
		List<TransactionTO> savedTransaction1 = transactionService.saveTransaction(getTransactionRealisedWithAnotherDate(savedCustomer.getId(), productIdList));

		//when
		customerService.deleteCustomer(savedCustomer);
		
		//then
		assertFalse(transactionService.contains(savedTransaction1.get(0).getId()));
	}

	private TransactionTO getTransactionRealised(Long customerId, Collection<Long> productIds){
		return new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 15).getTime())
				.withCustomerId(customerId)
				.withProductIds(productIds)
				.withTransactionStatus(TransactionStatus.REALISED)
				.build();
	}
	
	private TransactionTO getTransactionRealisedWithAnotherDate(Long customerId, Collection<Long> productIds){
		return new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 30).getTime())
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
	
	private TransactionTO getTransactionPreparedForDelivery(Long customerId, Collection<Long> productIds){
		return new TransactionTOBuilder()
				.withDate(new GregorianCalendar(2018, 7, 25).getTime())
				.withCustomerId(customerId)
				.withProductIds(productIds)
				.withTransactionStatus(TransactionStatus.REALISED)
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
				.withName("Cheap product")
				.withMarginPercent(30)
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
