package com.capgemini.jstk.transactionregistration.service;

import com.capgemini.jstk.transactionregistration.types.CustomerTO;

public interface CustomerService {
	public CustomerTO saveCustomer(CustomerTO customer);
	public CustomerTO updateCustomer(CustomerTO customer);
	public CustomerTO deleteCustomer(CustomerTO customer);
	public boolean contains(Long id);
	public int size();
	public CustomerTO findCustomerById(Long id);
}
