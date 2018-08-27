package com.capgemini.jstk.transactionregistration.dao;

import org.springframework.data.repository.CrudRepository;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long>, TransactionRepositoryCustom {


}
