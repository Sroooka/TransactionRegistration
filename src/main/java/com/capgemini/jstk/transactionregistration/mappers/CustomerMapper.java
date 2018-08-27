package com.capgemini.jstk.transactionregistration.mappers;

import java.util.Collection;
import java.util.stream.Collectors;

import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;
import com.capgemini.jstk.transactionregistration.types.CustomerTO;
import com.capgemini.jstk.transactionregistration.types.CustomerTO.CustomerTOBuilder;

public class CustomerMapper {
	
	public static CustomerTO toCustomerTO(CustomerEntity customerEntity) {
		
		if (customerEntity == null) {
			return null;
		}
		
		return new CustomerTOBuilder()
				.withId(customerEntity.getId())
				.withName(customerEntity.getName())
				.withSurname(customerEntity.getSurname())
				.withEmail(customerEntity.getEmail())
				.withPhone(customerEntity.getPhone())
				.withAddress(customerEntity.getAddress())
				.withBirth(customerEntity.getBirth())
				.withTrasactionIds(customerEntity.getTransactions()
						.stream()
						.map(s -> s.getId())
						.collect(Collectors.toSet()))
				.build();
	}
	
	public static CustomerEntity toCustomerEntity(CustomerTO customerTO) {
		
		if (customerTO == null) {
			return null;
		}
		
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setName(customerTO.getName());
		customerEntity.setSurname(customerTO.getSurname());
		customerEntity.setEmail(customerTO.getEmail());
		customerEntity.setPhone(customerTO.getPhone());
		customerEntity.setAddress(customerTO.getAddress());
		customerEntity.setBirth(customerTO.getBirth());
		return customerEntity;
	}
	
	public static Collection<CustomerTO> map2TOs(Collection<CustomerEntity> customerEntities) {
		return customerEntities.stream().map(CustomerMapper::toCustomerTO).collect(Collectors.toSet());
	}
	
	public static Collection<CustomerEntity> map2Entities(Collection<CustomerTO> customerTOs) {
		return customerTOs.stream().map(CustomerMapper::toCustomerEntity).collect(Collectors.toSet());
	}
}
