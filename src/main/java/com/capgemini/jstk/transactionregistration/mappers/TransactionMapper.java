package com.capgemini.jstk.transactionregistration.mappers;

import java.util.Collection;
import java.util.stream.Collectors;

import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.types.TransactionTO;
import com.capgemini.jstk.transactionregistration.types.TransactionTO.TransactionTOBuilder;

public class TransactionMapper {
	
	public static TransactionTO toTransactionTO(TransactionEntity transactionEntity) {
		if (transactionEntity == null) {
			return null;
		}
		return new TransactionTOBuilder()
				.withId(transactionEntity.getId())
				.withDate(transactionEntity.getDate())
				.withTransactionStatus(transactionEntity.getStatus())
				.withProductsAmount(transactionEntity.getProductsAmount())
				.withCustomerId(transactionEntity.getCustomer().getId())
				.withProductIds(transactionEntity.getProducts()
						.stream()
						.map(s -> s.getId())
						.collect(Collectors.toSet()))
				.build();
	}
	
	public static TransactionEntity toTransactionEntity(TransactionTO transactionTO) {
		if (transactionTO == null) {
			return null;
		}
		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setDate(transactionTO.getDate());
		transactionEntity.setStatus(transactionTO.getStatus());
		transactionEntity.setProductsAmount(transactionTO.getProductsAmount());
		return transactionEntity;
	}
	
	public static Collection<TransactionTO> map2TOs(Collection<TransactionEntity> transactionEntities) {
		return transactionEntities.stream().map(TransactionMapper::toTransactionTO).collect(Collectors.toSet());
	}
	
	public static Collection<TransactionEntity> map2Entities(Collection<TransactionTO> transactionTOs) {
		return transactionTOs.stream().map(TransactionMapper::toTransactionEntity).collect(Collectors.toSet());
	}
}
