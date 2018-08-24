package com.capgemini.jstk.transactionregistration.dao;

import java.util.List;
import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;

public interface TransactionRepositoryCustom {
	List<TransactionEntity> findByProductsAmount(int amount);
}
