package com.capgemini.jstk.transactionregistration.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.dao.CustomerRepository;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.exceptions.NoSuchCustomerInDatabaseException;
import com.capgemini.jstk.transactionregistration.mappers.CustomerMapper;
import com.capgemini.jstk.transactionregistration.service.CustomerService;
import com.capgemini.jstk.transactionregistration.types.CustomerTO;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;

	@Transactional(readOnly = false)
	@Override
	public CustomerTO saveCustomer(CustomerTO customer) {
		return CustomerMapper.toCustomerTO(customerRepository.save(CustomerMapper.toCustomerEntity(customer)));
	}

	@Transactional(readOnly = false)
	@Override
	public CustomerTO updateCustomer(CustomerTO customer) {
		if(!customerRepository.exists(customer.getId())){
			throw new NoSuchCustomerInDatabaseException("ID not found!");
		}
		CustomerEntity customerEntity = customerRepository.findOne(customer.getId());
		customerEntity.setName(customer.getName());
		customerEntity.setSurname(customer.getSurname());
		customerEntity.setEmail(customer.getEmail());
		customerEntity.setPhone(customer.getPhone());
		customerEntity.setAddress(customer.getAddress());
		customerEntity.setBirth(customer.getBirth());
		customerRepository.save(customerEntity);
		return CustomerMapper.toCustomerTO(customerEntity);
	}

	@Transactional(readOnly = false)
	@Override
	public CustomerTO deleteCustomer(CustomerTO customer) {
		if(!customerRepository.exists(customer.getId())){
			throw new NoSuchCustomerInDatabaseException("ID not found!");
		}
		customerRepository.delete(customer.getId());
		return customer;
	}

	@Override
	public boolean contains(Long id) {
		return customerRepository.exists(id);
	}

	@Override
	public int size() {
		if (customerRepository.findAll() instanceof Collection) {
		    return ((Collection<?>) customerRepository.findAll()).size();
		}
		return -1;
	}

	@Override
	public CustomerTO findCustomerById(Long id) {
		if(!customerRepository.exists(id)){
			throw new NoSuchCustomerInDatabaseException("ID not found!");
		}
		return CustomerMapper.toCustomerTO(customerRepository.findOne(id));
	}
}
