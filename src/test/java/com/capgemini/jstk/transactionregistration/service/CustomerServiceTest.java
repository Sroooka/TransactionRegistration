package com.capgemini.jstk.transactionregistration.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.management.InvalidApplicationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.exceptions.NoSuchCustomerInDatabaseException;
import com.capgemini.jstk.transactionregistration.types.CustomerTO;
import com.capgemini.jstk.transactionregistration.types.CustomerTO.CustomerTOBuilder;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest (properties = "spring.profiles.active=mysql-test")
//@SpringBootTest (properties = "spring.profiles.active=hsql-test")
public class CustomerServiceTest {

	@Autowired
	CustomerService customerService;
	
	@Test
	public void shouldAddAndFindCustomerById(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		
		//when
		CustomerTO foundCustomer = customerService.findCustomerById(savedCustomer.getId());
		
		// then
		assertEquals(foundCustomer.getName(), "Jan");
		assertEquals(foundCustomer.getSurname(), "Kowalski");
		assertEquals(foundCustomer.getEmail(), "jan.kowalski@domain.com");
		assertEquals(foundCustomer.getPhone(), "123-123-123");
		assertEquals(foundCustomer.getAddress(), "Poznan, ul. Poznanska 500");
		assertEquals(foundCustomer.getBirth(), new GregorianCalendar(1990, 12, 7).getTime());
	}
	
	@Test (expected = NoSuchCustomerInDatabaseException.class)
	public void shouldThrowExceptionWhenFindingNonExistingCustomer(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		
		//when then
		customerService.findCustomerById(savedCustomer.getId() + 123123L);
	}
	
	@Test
	public void shouldUpdateCustomer(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO updatedCustomer = getCustomerNowak();
		updatedCustomer.setId(savedCustomer.getId());
		
		//when
		CustomerTO returnedUpdatedCustomer = customerService.updateCustomer(updatedCustomer);
		
		// then
		assertEquals(returnedUpdatedCustomer.getId(), savedCustomer.getId());
		assertEquals(returnedUpdatedCustomer.getName(), "Krzysztof");
		assertEquals(returnedUpdatedCustomer.getSurname(), "Nowak");
		assertEquals(returnedUpdatedCustomer.getEmail(), "krzysztof.nowak@domain.com");
		assertEquals(returnedUpdatedCustomer.getPhone(), "321-321-321");
		assertEquals(returnedUpdatedCustomer.getAddress(), "Poznan, ul. Dluga 1");
		assertEquals(returnedUpdatedCustomer.getBirth(), new GregorianCalendar(1990, 12, 7).getTime());
	}
	
	@Test (expected = NoSuchCustomerInDatabaseException.class)
	public void shouldReturnErrorWhenUpdatingNonExistingCustomer(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		CustomerTO updatedCustomer = getCustomerNowak();
		updatedCustomer.setId(savedCustomer.getId() + 1);
		
		//when then
		customerService.updateCustomer(updatedCustomer);
	}
	
	@Test
	public void shouldDeleteCustomer(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		
		//when
		customerService.deleteCustomer(savedCustomer);
		
		// then
		assertFalse(customerService.contains(savedCustomer.getId()));
	}
	
	@Test (expected = NoSuchCustomerInDatabaseException.class)
	public void shouldReturnErrorWhenDeletingNonExistingCustomer(){
		//given
		CustomerTO savedCustomer = customerService.saveCustomer(getCustomerKowalski());
		savedCustomer.setId(savedCustomer.getId() + 123123L);
		
		//when then
		customerService.deleteCustomer(savedCustomer);
	}
	
	@Test
	public void shouldReturnCorrectSize(){
		//given when
		int sizeBeforeAdding = customerService.size();
		for(int i=0; i<10; i++){
			customerService.saveCustomer(getCustomerKowalski());
		}
		int sizeAfterAdding = customerService.size();
		
		// then
		assertEquals(10, sizeAfterAdding - sizeBeforeAdding);
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
}
