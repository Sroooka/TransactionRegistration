package com.capgemini.jstk.transactionregistration.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collection;
import java.util.GregorianCalendar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.enums.TransactionStatus;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchTransactionInDatabaseException;
import com.capgemini.jstk.transactionregistration.types.TransactionTO;
import com.capgemini.jstk.transactionregistration.types.TransactionTO.TransactionTOBuilder;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest (properties = "spring.profiles.active=mysql-test")
//@SpringBootTest (properties = "spring.profiles.active=hsql-test")
public class TransactionServicetest {

	@Autowired
	TransactionService transactionService;
	
	@Test
	public void shouldAddAndFindTransactionById(){
		//given
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(1L, null));
		
		//when
		TransactionTO foundTransaction = transactionService.findTransactionById(savedTransaction.getId());
		
		// then
		assertEquals(foundTransaction.getId(), savedTransaction.getId());
		assertEquals(foundTransaction.getDate(), new GregorianCalendar(2018, 7, 15).getTime());
		assertEquals(foundTransaction.getCustomerId(), new Long(1));
		assertEquals(foundTransaction.getStatus(), TransactionStatus.REALISED);
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldThrowExceptionWhenFindingNonExistingTransaction(){
		//given
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(1L, null));
		
		//when then
		transactionService.findTransactionById(savedTransaction.getId() + 123123L);
	}
	
	@Test
	public void shouldUpdateTransaction(){
		//given
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(1L, null));
		TransactionTO updatedTransaction = getTransactionCanceled(123123L, null);
		updatedTransaction.setId(savedTransaction.getId());
		
		//when
		TransactionTO returnedUpdatedTransaction = transactionService.updateTransaction(updatedTransaction);
		
		// then
		assertEquals(returnedUpdatedTransaction.getId(), savedTransaction.getId());
		assertEquals(savedTransaction.getStatus(), TransactionStatus.REALISED);
		assertEquals(returnedUpdatedTransaction.getStatus(), TransactionStatus.CANCELED);
		assertEquals(returnedUpdatedTransaction.getDate(), new GregorianCalendar(2018, 7, 25).getTime());
		assertEquals(savedTransaction.getCustomerId(), new Long(1));
		assertEquals(returnedUpdatedTransaction.getCustomerId(), new Long(123123));
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldReturnErrorWhenUpdatingNonExistingTransaction(){
		//given
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(1L, null));
		TransactionTO updatedTransaction = getTransactionRealised(1L, null);
		updatedTransaction.setId(savedTransaction.getId() + 1);
		
		//when then
		transactionService.updateTransaction(updatedTransaction);
	}
	
	@Test
	public void shouldDeleteTransaction(){
		//given
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(1L, null));
		
		//when
		transactionService.deleteTransaction(savedTransaction);
		
		// then
		assertFalse(transactionService.contains(savedTransaction.getId()));
	}
	
	@Test (expected = NoSuchTransactionInDatabaseException.class)
	public void shouldReturnErrorWhenDeletingNonExistingTransaction(){
		//given
		TransactionTO savedTransaction = transactionService.saveTransaction(getTransactionRealised(1L, null));
		savedTransaction.setId(savedTransaction.getId() + 123123L);
		
		//when then
		transactionService.deleteTransaction(savedTransaction);
	}
	
	@Test
	public void shouldReturnCorrectSize(){
		//given when
		int sizeBeforeAdding = transactionService.size();
		for(int i=0; i<10; i++){
			transactionService.saveTransaction(getTransactionRealised(1L, null));
		}
		int sizeAfterAdding = transactionService.size();
		
		// then
		assertEquals(10, sizeAfterAdding - sizeBeforeAdding);
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

}
