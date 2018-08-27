package com.capgemini.jstk.transactionregistration.dao;

import org.springframework.data.repository.CrudRepository;
import com.capgemini.jstk.transactionregistration.domain.CustomerEntity;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long>,  CustomerRepositoryCustom {

}