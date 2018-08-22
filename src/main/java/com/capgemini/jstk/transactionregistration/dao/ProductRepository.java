package com.capgemini.jstk.transactionregistration.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.jstk.transactionregistration.domain.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long>, ProductRepositoryCustom {

}
