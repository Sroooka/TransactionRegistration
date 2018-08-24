package com.capgemini.jstk.transactionregistration.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.capgemini.jstk.transactionregistration.domain.TransactionEntity;
import com.capgemini.jstk.transactionregistration.types.TransactionTO;
import com.capgemini.jstk.transactionregistration.types.TransactionTO.TransactionTOBuilder;

public class TransactionMapper {
	
	public static TransactionTO toTransactionTO(TransactionEntity transactionEntity) {
		if (transactionEntity == null) {
			return null;
		}
		Long customerId = (transactionEntity.getCustomer() == null) ? -1L : transactionEntity.getCustomer().getId();
		return new TransactionTOBuilder()
				.withId(transactionEntity.getId())
				.withDate(transactionEntity.getDate())
				.withTransactionStatus(transactionEntity.getStatus())
				.withProductsAmount(transactionEntity.getProductsAmount())
				.withCustomerId(customerId)
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
	
	public static List<TransactionTO> map2TOs(Collection<TransactionEntity> transactionEntities) {
		return transactionEntities.stream().map(TransactionMapper::toTransactionTO).collect(Collectors.toList());
	}
	
	public static List<TransactionEntity> map2Entities(Collection<TransactionTO> transactionTOs) {
		return transactionTOs.stream().map(TransactionMapper::toTransactionEntity).collect(Collectors.toList());
	}
}
